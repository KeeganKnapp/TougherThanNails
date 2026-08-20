package dev.keeganknapp.musicsync;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Server-side reimplementation of the MusicSyncDP datapack's four function files:
 * load.mcfunction, 100tick.mcfunction, 20tick.mcfunction, tick.mcfunction, and the four
 * music/*.mcfunction "pick a new song" routines. See each method's javadoc for which
 * original file/section it reproduces.
 */
public final class MusicSyncManager {
    private MusicSyncManager() {
    }

    /** Identifiers for every biome the original datapack treats as "underwater". */
    private static final Set<Identifier> OCEAN_BIOMES = Set.of(
            Identifier.fromNamespaceAndPath("minecraft", "ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "deep_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "deep_frozen_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "deep_lukewarm_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "cold_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "frozen_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "lukewarm_ocean"),
            Identifier.fromNamespaceAndPath("minecraft", "warm_ocean"),
            // Modded biome from Terralith. This is a harmless plain-identifier comparison:
            // if Terralith isn't installed, no biome will ever have this id, so this simply
            // never matches - it does not require the mod to be present.
            Identifier.fromNamespaceAndPath("terralith", "deep_warm_ocean")
    );

    // --- Per-area countdown counters, equivalent to the song_duration_left scoreboard --------
    private static final Map<Area, Integer> songDurationLeft = new HashMap<>();

    // --- Per-player runtime state, equivalent to the "tag"-based state in the datapack --------

    /** Equivalent of the music_current_overworld/nether/end/underwater tags: at most one area. */
    private static final Map<UUID, Area> currentMusicArea = new HashMap<>();

    /**
     * Equivalent of the underwater_music_sync tag. This is intentionally NOT recomputed every
     * tick - it only changes when music/water.mcfunction's pick routine runs, and is read by
     * music/overworld.mcfunction's eligibility check exactly as it was left by the last water
     * pick (see 20tick.mcfunction: water is picked before overworld each cycle, so a same-tick
     * water pick excludes those players from a same-tick overworld pick; on a tick where only
     * overworld repicks, this flag is deliberately stale).
     */
    private static final Set<UUID> underwaterMusicSync = new HashSet<>();

    /** Equivalent of the area_overworld/nether/end/underwater tags. Recomputed every 100 ticks. */
    private static final Map<UUID, Area> playerZoneArea = new HashMap<>();

    private static int serverTickCounter = 0;

    // --- delayed stop-then-play ----------------------------------------------------------------

    /**
     * Ticks to wait after a stopsound before sending the following playsound for a MUSIC-category
     * *streamed* track. Confirmed by live testing: an immediate stopsound + playsound back-to-back
     * silently fails to start the new streamed sound client-side (a plain, non-streamed sound has
     * no such issue - only streamed MUSIC-category playback races with its own teardown), so every
     * real "change what's playing" call goes through {@link #changeMusic} instead of calling
     * SoundSender directly.
     */
    private static final int SOUND_CHANGE_DELAY_TICKS = 5;

    private static final class PendingPlay {
        int ticksLeft;
        final String soundId;

        PendingPlay(int ticksLeft, String soundId) {
            this.ticksLeft = ticksLeft;
            this.soundId = soundId;
        }
    }

    private static final Map<UUID, PendingPlay> pendingPlays = new HashMap<>();

    /** Stops whatever's currently playing and schedules {@code soundId} to start shortly after. */
    private static void changeMusic(ServerPlayer player, String soundId) {
        SoundSender.stopMusic(player);
        pendingPlays.put(player.getUUID(), new PendingPlay(SOUND_CHANGE_DELAY_TICKS, soundId));
    }

    private static void cancelPendingPlay(ServerPlayer player) {
        pendingPlays.remove(player.getUUID());
    }

    private static void tickPendingPlays(List<ServerPlayer> players) {
        if (pendingPlays.isEmpty()) {
            return;
        }
        for (ServerPlayer player : players) {
            PendingPlay pending = pendingPlays.get(player.getUUID());
            if (pending == null) {
                continue;
            }
            if (--pending.ticksLeft <= 0) {
                SoundSender.playMusic(player, pending.soundId);
                pendingPlays.remove(player.getUUID());
            }
        }
    }

    // --- /musicsync skip voting --------------------------------------------------------------

    private static final int VOTE_DURATION_TICKS = 30 * 20;

    /** UUIDs of players who've voted to skip the current track, per area with an active vote. */
    private static final Map<Area, Set<UUID>> voteVoters = new HashMap<>();
    /** Ticks remaining before an area's active vote expires unresolved. */
    private static final Map<Area, Integer> voteTicksLeft = new HashMap<>();

    // --- waiting room / merge lane for players joining an area mid-song ----------------------

    /** Upper bound (inclusive) of every random gap rolled while building/playing a filler schedule: 20s. */
    private static final int FILLER_BUFFER_MAX_TICKS = 20 * 20;
    /**
     * Below this much remaining time until the area's next real sync, don't bother with a
     * waiting room at all - the real sync is close enough that any filler song/messaging would
     * just be noise.
     */
    private static final int MIN_WAIT_FOR_ROOM_TICKS = 40;

    /** One scheduled filler play: {@code offsetTicks} after entering the waiting room. */
    private record ScheduledFiller(int offsetTicks, Track track) {
    }

    private static final class WaitingRoom {
        final Area area;
        final List<ScheduledFiller> schedule;
        int ticksSinceEntry = 0;
        int nextIndex = 0;

        WaitingRoom(Area area, List<ScheduledFiller> schedule) {
            this.area = area;
            this.schedule = schedule;
        }
    }

    /** Players currently waiting to be swept up into an area's main song sync. */
    private static final Map<UUID, WaitingRoom> waitingRooms = new HashMap<>();

    // --- load.mcfunction -------------------------------------------------------------------

    /** Reproduces load.mcfunction: reset every counter to 0 and clear all runtime tag state. */
    public static void onServerStarted(MinecraftServer server) {
        for (Area area : Area.values()) {
            songDurationLeft.put(area, 0);
        }
        currentMusicArea.clear();
        underwaterMusicSync.clear();
        playerZoneArea.clear();
        voteVoters.clear();
        voteTicksLeft.clear();
        waitingRooms.clear();
        pendingPlays.clear();
        serverTickCounter = 0;
    }

    /** Reproduces the tag-cleanup half of load.mcfunction for a single disconnecting player. */
    public static void onPlayerDisconnect(ServerPlayer player) {
        UUID id = player.getUUID();
        currentMusicArea.remove(id);
        underwaterMusicSync.remove(id);
        playerZoneArea.remove(id);
        waitingRooms.remove(id);
        pendingPlays.remove(id);
    }

    // --- main tick dispatch ------------------------------------------------------------------

    /**
     * Call once per server tick (ServerTickEvents.END_SERVER_TICK). Reproduces the schedule
     * cadence set up by load.mcfunction: tick.mcfunction runs every tick, 20tick.mcfunction
     * every 20 ticks, and 100tick.mcfunction every 100 ticks.
     */
    public static void onServerTick(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        runHardCutTick(players);
        tickVotes(players);
        admitToWaitingRooms(players);
        tickWaitingRoomPlayback(players);
        tickPendingPlays(players);

        serverTickCounter++;
        if (serverTickCounter % 20 == 0) {
            run20TickCycle(players);
        }
        if (serverTickCounter % 100 == 0) {
            run100TickCycle(players);
        }
    }

    // --- tick.mcfunction ---------------------------------------------------------------------

    /**
     * Reproduces tick.mcfunction exactly: hard-cut (stopsound + untag) a player's current-area
     * music the instant they leave that area's "home" dimension. Overworld and underwater both
     * treat minecraft:overworld as home, so a plain water&lt;-&gt;land transition within the
     * overworld is deliberately NOT a hard cut here - only leaving the overworld dimension
     * entirely cuts overworld/underwater music.
     */
    private static void runHardCutTick(List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Area area = currentMusicArea.get(player.getUUID());
            if (area == null) {
                continue;
            }
            var dimension = player.level().dimension();
            boolean stillHome = switch (area) {
                case NETHER -> dimension.equals(Level.NETHER);
                case END -> dimension.equals(Level.END);
                case OVERWORLD, UNDERWATER -> dimension.equals(Level.OVERWORLD);
            };
            if (!stillHome) {
                SoundSender.stopMusic(player);
                cancelPendingPlay(player);
                currentMusicArea.remove(player.getUUID());
            }
        }
    }

    // --- 100tick.mcfunction ------------------------------------------------------------------

    /**
     * Reproduces 100tick.mcfunction: re-tag every online player with exactly one area based on
     * dimension, with an ocean-biome override in the overworld. This state (playerZoneArea) is
     * purely informational parity with the original tags - nothing else in the system consumes
     * it, exactly as in the original datapack where area_* tags are set here but never read
     * anywhere else.
     */
    private static void run100TickCycle(List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Area zone = computeZoneArea(player);
            if (zone == null) {
                playerZoneArea.remove(player.getUUID());
            } else {
                playerZoneArea.put(player.getUUID(), zone);
            }
        }
    }

    private static Area computeZoneArea(ServerPlayer player) {
        var dimension = player.level().dimension();
        if (dimension.equals(Level.NETHER)) {
            return Area.NETHER;
        }
        if (dimension.equals(Level.END)) {
            return Area.END;
        }
        if (dimension.equals(Level.OVERWORLD)) {
            return isInOceanBiome(player) ? Area.UNDERWATER : Area.OVERWORLD;
        }
        return null;
    }

    private static boolean isInOceanBiome(ServerPlayer player) {
        BlockPos pos = player.blockPosition();
        Holder<Biome> biome = player.level().getBiome(pos);
        for (Identifier oceanBiome : OCEAN_BIOMES) {
            if (biome.is(oceanBiome)) {
                return true;
            }
        }
        return false;
    }

    // --- 20tick.mcfunction -------------------------------------------------------------------

    /**
     * Reproduces 20tick.mcfunction precisely: for every area whose counter is &lt;= 0, run its
     * pick routine (in the original file's order: underwater, nether, end, overworld); THEN,
     * for every area whose counter is &gt;= 1 (including one that was just set by a pick that
     * ran moments ago in this same cycle - matching the original's execution order), subtract
     * 20 from it.
     */
    private static void run20TickCycle(List<ServerPlayer> players) {
        if (songDurationLeft.get(Area.UNDERWATER) <= 0) {
            pickUnderwater(players);
        }
        if (songDurationLeft.get(Area.NETHER) <= 0) {
            pickNether(players);
        }
        if (songDurationLeft.get(Area.END) <= 0) {
            pickEnd(players);
        }
        if (songDurationLeft.get(Area.OVERWORLD) <= 0) {
            pickOverworld(players);
        }

        for (Area area : Area.values()) {
            int left = songDurationLeft.get(area);
            if (left >= 1) {
                songDurationLeft.put(area, left - 20);
            }
        }
    }

    // --- music/*.mcfunction pick routines -----------------------------------------------------

    /** Reproduces music/water.mcfunction: random value 1..3, padding 500..2000. */
    private static boolean pickUnderwater(List<ServerPlayer> players) {
        underwaterMusicSync.clear();
        List<ServerPlayer> eligible = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (isInOceanBiome(player)) {
                underwaterMusicSync.add(player.getUUID());
                eligible.add(player);
            }
        }
        if (eligible.isEmpty()) {
            return false;
        }
        int roll = ThreadLocalRandom.current().nextInt(1, 4); // 1..3 inclusive
        int padding = ThreadLocalRandom.current().nextInt(500, 2001); // 500..2000 inclusive
        applyPick(Area.UNDERWATER, Tracks.UNDERWATER[roll - 1], eligible, padding);
        return true;
    }

    /** Reproduces music/nether.mcfunction: random value 1..4, padding 4000..8000. */
    private static boolean pickNether(List<ServerPlayer> players) {
        List<ServerPlayer> eligible = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (player.level().dimension().equals(Level.NETHER)) {
                eligible.add(player);
            }
        }
        if (eligible.isEmpty()) {
            return false;
        }
        int roll = ThreadLocalRandom.current().nextInt(1, 5); // 1..4 inclusive
        int padding = ThreadLocalRandom.current().nextInt(4000, 8001); // 4000..8000 inclusive
        applyPick(Area.NETHER, Tracks.NETHER[roll - 1], eligible, padding);
        return true;
    }

    /** Reproduces music/end.mcfunction: random value 1..2, padding 4000..8000. */
    private static boolean pickEnd(List<ServerPlayer> players) {
        List<ServerPlayer> eligible = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (player.level().dimension().equals(Level.END)) {
                eligible.add(player);
            }
        }
        if (eligible.isEmpty()) {
            return false;
        }
        int roll = ThreadLocalRandom.current().nextInt(1, 3); // 1..2 inclusive
        int padding = ThreadLocalRandom.current().nextInt(4000, 8001); // 4000..8000 inclusive
        applyPick(Area.END, Tracks.END[roll - 1], eligible, padding);
        return true;
    }

    /**
     * Reproduces music/overworld.mcfunction: random value 1..N (N = Tracks.OVERWORLD.length),
     * padding 500..1500. Eligible players are those in the overworld dimension AND NOT currently
     * flagged by the (possibly stale) underwater_music_sync state - see the
     * {@link #underwaterMusicSync} javadoc.
     */
    private static boolean pickOverworld(List<ServerPlayer> players) {
        List<ServerPlayer> eligible = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (player.level().dimension().equals(Level.OVERWORLD)
                    && !underwaterMusicSync.contains(player.getUUID())) {
                eligible.add(player);
            }
        }
        if (eligible.isEmpty()) {
            return false;
        }
        int roll = ThreadLocalRandom.current().nextInt(1, Tracks.OVERWORLD.length + 1);
        int padding = ThreadLocalRandom.current().nextInt(500, 1501); // 500..1500 inclusive
        applyPick(Area.OVERWORLD, Tracks.OVERWORLD[roll - 1], eligible, padding);
        return true;
    }

    // --- /musicsync skip command support ------------------------------------------------------

    /**
     * Forces an immediate re-roll for the given area, ignoring its remaining countdown -
     * equivalent to manually re-running that area's music/*.mcfunction pick routine early.
     * Returns false (and does nothing) if nobody is currently eligible for that area.
     */
    public static boolean skipArea(Area area, MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        return switch (area) {
            case UNDERWATER -> pickUnderwater(players);
            case NETHER -> pickNether(players);
            case END -> pickEnd(players);
            case OVERWORLD -> pickOverworld(players);
        };
    }

    /** The area a given player currently counts toward, per the same rules as 100tick.mcfunction. */
    public static Area zoneAreaOf(ServerPlayer player) {
        return computeZoneArea(player);
    }

    /** Every online player who currently counts toward the given area. */
    public static List<ServerPlayer> eligiblePlayers(Area area, List<ServerPlayer> players) {
        List<ServerPlayer> result = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (computeZoneArea(player) == area) {
                result.add(player);
            }
        }
        return result;
    }

    /** Outcome of casting (or attempting to cast) a skip vote. */
    public record VoteOutcome(boolean eligible, boolean alreadyVoted, boolean passed,
                               int votes, int total, int needed) {
    }

    /**
     * Casts {@code voter}'s vote to skip the current track in {@code area}. A vote session
     * starts on the first vote for an area and lasts {@link #VOTE_DURATION_TICKS} (30s); a new
     * vote after that window (or with no active session) starts a fresh one. Once votes reach a
     * strict majority of players currently in the area, the track is skipped immediately and the
     * session is cleared.
     */
    public static VoteOutcome castSkipVote(Area area, ServerPlayer voter, MinecraftServer server) {
        List<ServerPlayer> eligible = eligiblePlayers(area, server.getPlayerList().getPlayers());
        if (!eligible.contains(voter)) {
            return new VoteOutcome(false, false, false, 0, eligible.size(), 0);
        }

        Set<UUID> voters = voteVoters.computeIfAbsent(area, a -> new HashSet<>());
        Integer ticksLeft = voteTicksLeft.get(area);
        if (ticksLeft == null || ticksLeft <= 0) {
            voters.clear();
            voteTicksLeft.put(area, VOTE_DURATION_TICKS);
        }

        // Drop votes from players who are no longer around/eligible before counting.
        Set<UUID> eligibleIds = new HashSet<>();
        for (ServerPlayer player : eligible) {
            eligibleIds.add(player.getUUID());
        }
        voters.retainAll(eligibleIds);

        boolean alreadyVoted = !voters.add(voter.getUUID());
        int needed = eligible.size() / 2 + 1;

        if (voters.size() >= needed) {
            voteVoters.remove(area);
            voteTicksLeft.remove(area);
            skipArea(area, server);
            return new VoteOutcome(true, alreadyVoted, true, voters.size(), eligible.size(), needed);
        }
        return new VoteOutcome(true, alreadyVoted, false, voters.size(), eligible.size(), needed);
    }

    /** Ticks down and expires (clearing votes with no announcement) any active vote sessions. */
    private static void tickVotes(List<ServerPlayer> players) {
        if (voteTicksLeft.isEmpty()) {
            return;
        }
        for (Area area : List.copyOf(voteTicksLeft.keySet())) {
            int left = voteTicksLeft.get(area) - 1;
            if (left <= 0) {
                voteTicksLeft.remove(area);
                voteVoters.remove(area);
                Component message = Component.literal(
                        "Not enough votes to skip the " + area.name().toLowerCase() + " track - vote expired.")
                        .withStyle(net.minecraft.ChatFormatting.GRAY);
                for (ServerPlayer player : eligiblePlayers(area, players)) {
                    player.sendSystemMessage(message);
                }
            } else {
                voteTicksLeft.put(area, left);
            }
        }
    }

    /**
     * Shared tail end of every pick routine: stopsound, tag the new current area, playsound,
     * send the "Now Playing" message, and set the area's counter to duration + padding. Anyone
     * who was still in that area's waiting room (mid-filler, or just waiting silently) gets
     * pulled out and interrupted here too - covers both a natural resync and a majority
     * skip-vote forcing an early one, since both funnel through this same method.
     */
    private static void applyPick(Area area, Track track, List<ServerPlayer> eligible, int padding) {
        Component message = buildNowPlayingMessage(area, track);
        for (ServerPlayer player : eligible) {
            boolean wasWaiting = waitingRooms.remove(player.getUUID()) != null;
            currentMusicArea.put(player.getUUID(), area);
            changeMusic(player, track.soundId());
            if (wasWaiting) {
                player.sendSystemMessage(Component.literal("♪ Synced!")
                        .withStyle(style -> style.withColor(net.minecraft.ChatFormatting.GREEN).withItalic(true)));
            }
            player.sendSystemMessage(message);
        }
        songDurationLeft.put(area, track.durationTicks() + padding);
    }

    // --- waiting room / merge lane -------------------------------------------------------------

    /**
     * Every tick, admits any online player who's eligible for an area but currently has no music
     * of their own playing (fresh join, or just hard-cut by {@link #runHardCutTick} from a
     * dimension change) and isn't already waiting for one - covers joining the server,
     * portaling into a new area, or anything else that would otherwise leave a player sitting in
     * silence. Re-entrant per player: does nothing once they're either synced or already waiting
     * for the area they're currently in.
     *
     * <p>Deliberately gated on {@code currentMusicArea} being null rather than on the player's
     * live zone differing from it: an overworld&lt;-&gt;underwater biome transition changes the
     * live zone constantly but is NOT a hard cut (see {@link #runHardCutTick}), so a player
     * mid-song who briefly wades into/out of an ocean biome must keep hearing whatever's already
     * playing rather than being yanked into a solo force-pick for the area they just stepped
     * into - that was cutting overworld tracks off mid-song for anyone near open water.
     *
     * <p>If nobody else is currently in that area, there's nothing to sync <em>with</em> - skip
     * the waiting room entirely and force an immediate fresh pick for the area instead, so a solo
     * player never waits out someone else's (or nobody's) leftover song.
     */
    private static void admitToWaitingRooms(List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Area zone = computeZoneArea(player);
            if (zone == null) {
                continue;
            }
            UUID id = player.getUUID();
            if (currentMusicArea.get(id) != null) {
                continue;
            }
            WaitingRoom existing = waitingRooms.get(id);
            if (existing != null && existing.area == zone) {
                continue;
            }
            if (isSoleOccupant(player, zone, players)) {
                waitingRooms.remove(id);
                forcePick(zone, players);
            } else {
                enterWaitingRoom(player, zone);
            }
        }
    }

    private static boolean isSoleOccupant(ServerPlayer player, Area zone, List<ServerPlayer> players) {
        for (ServerPlayer other : players) {
            if (other != player && computeZoneArea(other) == zone) {
                return false;
            }
        }
        return true;
    }

    private static void forcePick(Area area, List<ServerPlayer> players) {
        switch (area) {
            case UNDERWATER -> pickUnderwater(players);
            case NETHER -> pickNether(players);
            case END -> pickEnd(players);
            case OVERWORLD -> pickOverworld(players);
        }
    }

    private static void enterWaitingRoom(ServerPlayer player, Area area) {
        SoundSender.stopMusic(player);
        int remaining = songDurationLeft.getOrDefault(area, 0);
        if (remaining < MIN_WAIT_FOR_ROOM_TICKS) {
            // Real sync is imminent - let the next 20tick cycle pick them up directly, no point
            // announcing a wait that's barely going to happen.
            waitingRooms.remove(player.getUUID());
            return;
        }
        List<ScheduledFiller> schedule = buildFillerSchedule(area, remaining);
        waitingRooms.put(player.getUUID(), new WaitingRoom(area, schedule));
        player.sendSystemMessage(Component.literal("♪ Awaiting music sync...")
                .withStyle(style -> style.withColor(net.minecraft.ChatFormatting.DARK_GRAY).withItalic(true)));
    }

    /**
     * Builds a schedule of filler tracks to kill time until {@code remainingTicks} (however long
     * is left until this area's real sync point) runs out: a random 0-20s buffer, then a random
     * track that fits in what's left, then another random 0-20s buffer, then another track if
     * one still fits, and so on until nothing fits anymore (first-come-first-served - we don't
     * search for a smaller buffer to try to squeeze one more track in).
     */
    private static List<ScheduledFiller> buildFillerSchedule(Area area, int remainingTicks) {
        List<ScheduledFiller> schedule = new ArrayList<>();
        Track[] candidates = Tracks.forArea(area);

        int offset = rollBufferTicks();
        int budget = remainingTicks - offset;

        while (true) {
            int buffer = rollBufferTicks();
            Track chosen = randomTrackThatFits(candidates, budget - buffer);
            if (chosen == null) {
                break;
            }
            schedule.add(new ScheduledFiller(offset, chosen));
            int consumed = chosen.durationTicks() + buffer;
            offset += consumed;
            budget -= consumed;
        }
        return schedule;
    }

    private static int rollBufferTicks() {
        return ThreadLocalRandom.current().nextInt(0, FILLER_BUFFER_MAX_TICKS + 1);
    }

    private static Track randomTrackThatFits(Track[] candidates, int budget) {
        List<Track> fitting = new ArrayList<>();
        for (Track track : candidates) {
            if (track.durationTicks() <= budget) {
                fitting.add(track);
            }
        }
        if (fitting.isEmpty()) {
            return null;
        }
        return fitting.get(ThreadLocalRandom.current().nextInt(fitting.size()));
    }

    /** Advances every active waiting room by one tick, starting any filler track whose turn is up. */
    private static void tickWaitingRoomPlayback(List<ServerPlayer> players) {
        if (waitingRooms.isEmpty()) {
            return;
        }
        for (ServerPlayer player : players) {
            WaitingRoom room = waitingRooms.get(player.getUUID());
            if (room == null) {
                continue;
            }
            room.ticksSinceEntry++;
            while (room.nextIndex < room.schedule.size()
                    && room.ticksSinceEntry >= room.schedule.get(room.nextIndex).offsetTicks()) {
                Track track = room.schedule.get(room.nextIndex).track();
                changeMusic(player, track.soundId());
                room.nextIndex++;
            }
        }
    }

    private static Component buildNowPlayingMessage(Area area, Track track) {
        MutableComponent prefix = Component.literal("Now Playing: ")
                .withStyle(style -> style.withColor(net.minecraft.ChatFormatting.GRAY));
        MutableComponent title = Component.literal(track.title())
                .withStyle(style -> style.withColor(colorByName(area.titleColor)).withItalic(true));
        MutableComponent suffix = Component.literal(" (" + track.timeText() + ")")
                .withStyle(style -> style.withColor(net.minecraft.ChatFormatting.DARK_GRAY));
        return prefix.append(title).append(suffix);
    }

    private static net.minecraft.ChatFormatting colorByName(String name) {
        return switch (name) {
            case "green" -> net.minecraft.ChatFormatting.GREEN;
            case "red" -> net.minecraft.ChatFormatting.RED;
            case "light_purple" -> net.minecraft.ChatFormatting.LIGHT_PURPLE;
            case "blue" -> net.minecraft.ChatFormatting.BLUE;
            default -> net.minecraft.ChatFormatting.WHITE;
        };
    }
}
