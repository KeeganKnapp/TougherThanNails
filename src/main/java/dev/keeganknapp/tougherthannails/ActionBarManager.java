package dev.keeganknapp.tougherthannails;

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
public final class ActionBarManager {
    private ActionBarManager() {
    }

    /*
    /**
    // Identifiers for every biome the original datapack treats as "underwater".
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
    */

    private static int tickCounter = 0;

    public static void onServerTick(MinecraftServer server) {
        tickCounter++;
        // Only fire once every 20 ticks (exactly 1 second)
        if (tickCounter % 20 != 0) return; 

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            player.sendSystemMessage(Component.literal("             \uE791\uE791\uE791\uE784\uE784\uE784\uE784\uE784\uE784\uE784"), true); 
        }

    }

    private String buildActionBar(int thirst, int temperature) {
        //String base = "              ";
        String base = " \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789 \uE789\uE789\uE789 ";

        base += TemperatureIndicatorType.getGlyph(temperature) + "\uE789";

        for(int i = 20; i >= 1; i--) {
            if(i > thirst)
                base += ThirstBarType.EMPTY.getAsciiCode();
            else if(i == thirst && i % 2 == 1)
                base += ThirstBarType.HALF.getAsciiCode();
            else
                base += ThirstBarType.FULL.getAsciiCode();

        }

        return base;
    }


    /*
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
    */

   }
