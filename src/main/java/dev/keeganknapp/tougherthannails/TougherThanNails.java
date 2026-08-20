package dev.keeganknapp.tougherthannails;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

/**
 * Entry point for the MusicSync mod: an autohosted, server-side-only reimplementation of the
 * old MusicSyncRP/MusicSyncDP loose resource pack + datapack pair. Polymer's
 * polymer-resource-pack + polymer-autohost merge this mod's bundled assets/ folder into a
 * single generated resource pack and automatically host/push it to every joining vanilla
 * client - no manual zipping/uploading, and no client-side mod required.
 */
public final class TougherThanNails implements ModInitializer {
    @Override
    public void onInitialize() {
        // Registers this mod's own src/main/resources/assets/... (globalmusic sounds.json +
        // the minecraft sounds.json override that silences vanilla's music selector) as a
        // source Polymer merges into the single generated/autohosted resource pack.
        PolymerResourcePackUtils.addModAssets("tougherthannails");

        //ServerLifecycleEvents.SERVER_STARTED.register(MusicSyncManager::onServerStarted);
        //ServerTickEvents.END_SERVER_TICK.register(MusicSyncManager::onServerTick);
        //ServerPlayConnectionEvents.DISCONNECT.register(
        //      (handler, server) -> MusicSyncManager.onPlayerDisconnect(handler.getPlayer()));

        //CommandRegistrationCallback.EVENT.register(MusicSyncMod::registerCommands);
    }
    /**
     * Registers "/musicsync skip [overworld|nether|end|underwater]" - open to any player, no
     * permission required. Each run casts one vote to skip the current track for that area;
     * once votes reach a strict majority of the players currently in that area, it skips
     * immediately. A vote session expires after 30 seconds if majority isn't reached. With no
     * area given, it votes for whichever area the command's executing player currently counts
     * toward.
    private static void registerCommands(com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher,
                                          net.minecraft.commands.CommandBuildContext registryAccess,
                                          Commands.CommandSelection environment) {
        var skip = Commands.literal("musicsync")
                .then(Commands.literal("skip")
                        .executes(MusicSyncMod::voteExecutorsOwnArea)
                        .then(Commands.literal("overworld").executes(ctx -> voteArea(ctx, Area.OVERWORLD)))
                        .then(Commands.literal("nether").executes(ctx -> voteArea(ctx, Area.NETHER)))
                        .then(Commands.literal("end").executes(ctx -> voteArea(ctx, Area.END)))
                        .then(Commands.literal("underwater").executes(ctx -> voteArea(ctx, Area.UNDERWATER))))
                .then(Commands.literal("debugsound")
                        .then(Commands.literal("registered").executes(ctx -> debugSound(ctx, true)))
                        .then(Commands.literal("direct").executes(ctx -> debugSound(ctx, false)))
                        .then(Commands.literal("stopplay").executes(MusicSyncMod::debugStopPlay))
                        .then(Commands.literal("musicstopplay").executes(MusicSyncMod::debugMusicStopPlay)));
        dispatcher.register(skip);
    }

    /**
     * Temporary diagnostic: sends a plain, well-known vanilla sound
     * ({@code entity.experience_orb.pickup}) straight to the executor via a hand-built
     * ClientboundSoundPacket at SoundSource.PLAYERS - same packet-sending style as
     * {@link SoundSender}, but with a registered sound instead of a custom globalmusic:* one, and
     * a normal, obviously-not-muted category. "registered" wraps SoundEvents.EXPERIENCE_ORB_PICKUP
     * as a real Holder; "direct" builds it the same way SoundSender does for our custom events
     * (Holder.direct(SoundEvent.createVariableRangeEvent(id))), just with a registered id. If
     * neither variant is audible, the issue is generic to how we send packets from our own code
     * (not specific to MUSIC category or to unregistered custom sound ids).
    private static int debugSound(CommandContext<CommandSourceStack> ctx, boolean useRegisteredHolder)
            throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Holder<SoundEvent> sound = useRegisteredHolder
                ? net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EXPERIENCE_ORB_PICKUP)
                : Holder.direct(SoundEvent.createVariableRangeEvent(
                        Identifier.fromNamespaceAndPath("minecraft", "entity.experience_orb.pickup")));
        long seed = player.level().getRandom().nextLong();
        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                sound, SoundSource.PLAYERS,
                player.getX(), player.getY(), player.getZ(),
                1.0f, 1.0f, seed);
        player.connection.send(packet);
        ctx.getSource().sendSuccess(() -> Component.literal(
                "Sent debug sound (" + (useRegisteredHolder ? "registered" : "direct") + " holder, PLAYERS category)."),
                false);
        return 1;
    }

    /**
     * Same debug sound as {@link #debugSound}, but preceded by an immediate stopsound - the
     * general shape of every real trigger in this mod (always stop-then-play), isolated onto a
     * plain, non-streamed PLAYERS-category sound.
    private static int debugStopPlay(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        player.connection.send(new ClientboundStopSoundPacket(null, SoundSource.PLAYERS));
        Holder<SoundEvent> sound = Holder.direct(SoundEvent.createVariableRangeEvent(
                Identifier.fromNamespaceAndPath("minecraft", "entity.experience_orb.pickup")));
        long seed = player.level().getRandom().nextLong();
        player.connection.send(new ClientboundSoundPacket(sound, SoundSource.PLAYERS,
                player.getX(), player.getY(), player.getZ(), 1.0f, 1.0f, seed));
        ctx.getSource().sendSuccess(() -> Component.literal("Sent stop+play debug sound (PLAYERS category)."), false);
        return 1;
    }

    /**
     * The exact real failure pattern, isolated: stopMusic() then playMusic("sweden") via the
     * actual SoundSender class every real trigger in this mod uses.
    private static int debugMusicStopPlay(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        SoundSender.stopMusic(player);
        SoundSender.playMusic(player, "sweden");
        ctx.getSource().sendSuccess(() -> Component.literal("Sent stop+play debug sound (globalmusic:sweden, MUSIC category)."), false);
        return 1;
    }

    private static int voteExecutorsOwnArea(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Area area = MusicSyncManager.zoneAreaOf(player);
        if (area == null) {
            ctx.getSource().sendFailure(Component.literal(
                    "You're not in an area with synced music - specify one: /musicsync skip <overworld|nether|end|underwater>"));
            return 0;
        }
        return castVote(ctx.getSource(), player, area);
    }

    private static int voteArea(CommandContext<CommandSourceStack> ctx, Area area) throws CommandSyntaxException {
        return castVote(ctx.getSource(), ctx.getSource().getPlayerOrException(), area);
    }

    private static int castVote(CommandSourceStack source, ServerPlayer voter, Area area) {
        MusicSyncManager.VoteOutcome outcome = MusicSyncManager.castSkipVote(area, voter, source.getServer());
        String areaName = area.name().toLowerCase();

        if (!outcome.eligible()) {
            source.sendFailure(Component.literal(
                    "You need to be in the " + areaName + " area to vote to skip its music."));
            return 0;
        }

        if (outcome.passed()) {
            Component message = Component.literal(voter.getName().getString()
                    + " voted to skip - majority reached! Skipping the " + areaName + " track...")
                    .withStyle(ChatFormatting.GREEN);
            broadcastToArea(source, area, message);
            return 1;
        }

        if (outcome.alreadyVoted()) {
            source.sendSuccess(() -> Component.literal("You've already voted to skip this track ("
                    + outcome.votes() + "/" + outcome.total() + ").").withStyle(ChatFormatting.GRAY), false);
            return 1;
        }

        Component message = Component.literal(voter.getName().getString() + " votes to skip to the next song ("
                + outcome.votes() + "/" + outcome.total() + ")").withStyle(ChatFormatting.YELLOW);
        broadcastToArea(source, area, message);
        return 1;
    }

    private static void broadcastToArea(CommandSourceStack source, Area area, Component message) {
        for (ServerPlayer player : MusicSyncManager.eligiblePlayers(area, source.getServer().getPlayerList().getPlayers())) {
            player.sendSystemMessage(message);
        }
    }
    */
}
