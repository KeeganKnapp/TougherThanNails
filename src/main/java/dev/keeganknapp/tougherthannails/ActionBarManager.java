package dev.keeganknapp.tougherthannails;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import dev.keeganknapp.tougherthannails.ascii.TemperatureIndicatorType;
import dev.keeganknapp.tougherthannails.ascii.ThirstBarType;

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

    private static final HashSet<UUID> ACTIVE_HOT_OVERLAYS = new HashSet<>();

    public static void onServerTick(MinecraftServer server) {
        tickCounter++;
        // Only fire once every 20 ticks (exactly 1 second)

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {

            UUID uuid = player.getUUID();

            if(shouldShowColdOverlay(player)) 
                showColdOverlay(player);
            
            if(shouldShowHotOverlay(player) && !ACTIVE_HOT_OVERLAYS.contains(uuid))
                showHotOverlay(player);
            else if(!shouldShowHotOverlay(player) && ACTIVE_HOT_OVERLAYS.contains(uuid)) {
                removeHotOverlay(player);
            }
            

            //if(player.gameMode().isCreative()) continue;




            float playerTemperature = TemperatureManager.getPlayerTemperature(player);
            float playerThirstSaturation = ThirstManager.getPlayerThirstSaturation(player);


            int playerThirst = ThirstManager.getPlayerThirst(player);
            int roundedPlayerTemperature = Math.round(playerTemperature);
            int roundedPlayerThirstSaturation = Math.round(playerThirstSaturation);
            boolean underwater = player.getAirSupply() < player.getMaxAirSupply();
            boolean nudge = tickCounter % 40 == 0;
            boolean dehydration = player.hasEffect(ModStatusEffects.DEHYDRATION_HOLDER);

            
            Component actionBarMessage;
            //saturation nudge visualizer
            actionBarMessage = Component.literal(
                buildActionBar(playerThirst, roundedPlayerTemperature, roundedPlayerThirstSaturation, underwater, nudge, dehydration))
            .withStyle(style -> style.withShadowColor(0));



            player.sendSystemMessage(actionBarMessage, true);
        }

    }


    //need to implement movement of bar when player underwater
    private static String buildActionBar(int thirst, int temperature, int saturation, boolean underwater, boolean nudge, boolean dehydration) {
        //String base = "              ";
        //String base = "           ";
        String base = "          ";

        //base += TemperatureIndicatorType.getGlyph(temperature) + "";
        base += TemperatureIndicatorType.getGlyph(temperature) + "";


        if(underwater && !dehydration) {
            for(int i = 20; i >= 1; i-=2) {
                if(i > thirst && i-1 != thirst)
                base += ThirstBarType.EMPTY_UNDERWATER.getAsciiCode();
                else if(i > thirst && i-1 == thirst)
                base += ThirstBarType.HALF_UNDERWATER.getAsciiCode();
                else
                base += ThirstBarType.FULL_UNDERWATER.getAsciiCode();
            }
        }
        else if(dehydration && !underwater) {
            for(int i = 20; i >= 1; i-=2) {
                if(i > thirst && i-1 != thirst)
                base += ThirstBarType.EMPTY.getAsciiCode();
                else if(i > thirst && i-1 == thirst)
                base += ThirstBarType.HALF_DEHYDRATION.getAsciiCode();
                else
                base += ThirstBarType.FULL_DEHYDRATION.getAsciiCode();
            }
        }
        else if(dehydration && underwater) {
            for(int i = 20; i >= 1; i-=2) {
                if(i > thirst && i-1 != thirst)
                base += ThirstBarType.EMPTY_UNDERWATER.getAsciiCode();
                else if(i > thirst && i-1 == thirst)
                base += ThirstBarType.HALF_DEHYDRATION_UNDERWATER.getAsciiCode();
                else
                base += ThirstBarType.FULL_DEHYDRATION_UNDERWATER.getAsciiCode();
            }
        }
        else {
            if(nudge) {
                for(int i = 20; i >= 1; i-=2) {
                    if(i <= saturation) {
                        if(i > thirst && i-1 != thirst)
                        base += ThirstBarType.EMPTY_SATURATION_NUDGE.getAsciiCode();
                        else if(i > thirst && i-1 == thirst)
                        base += ThirstBarType.HALF_SATURATION_NUDGE.getAsciiCode();
                        else
                        base += ThirstBarType.FULL_SATURATION_NUDGE.getAsciiCode();
                    }
                    else {
                        if(i > thirst && i-1 != thirst)
                        base += ThirstBarType.EMPTY.getAsciiCode();
                        else if(i > thirst && i-1 == thirst)
                        base += ThirstBarType.HALF.getAsciiCode();
                        else
                        base += ThirstBarType.FULL.getAsciiCode();
                    }
                }
            }
            else {
                for(int i = 20; i >= 1; i-=2) {
                    if(i > thirst && i-1 != thirst)
                    base += ThirstBarType.EMPTY.getAsciiCode();
                    else if(i > thirst && i-1 == thirst)
                    base += ThirstBarType.HALF.getAsciiCode();
                    else
                    base += ThirstBarType.FULL.getAsciiCode();
                }
            }
        }

        return base;
    }


    private static void showColdOverlay(ServerPlayer player) {
        float playerTemp = TemperatureManager.getTemperatureRaw(player);

        BlockPos feetPos = BlockPos.containing(player.position());
        BlockPos eyePos = BlockPos.containing(player.getEyePosition());

        boolean inSnow = player.level().getBlockState(feetPos).is(Blocks.POWDER_SNOW) 
        || player.level().getBlockState(eyePos).is(Blocks.POWDER_SNOW);

        int currentTicks = player.getTicksFrozen();

        if (playerTemp < TTNTuningConfig.MIN_TEMP_FOR_NO_OVERLAY && !inSnow) {
            int targetTicksFrozen = (int) Math.max(0, 140 - (70 / 3) * playerTemp);

            if (currentTicks < targetTicksFrozen) {
                player.setTicksFrozen(Math.min(targetTicksFrozen, currentTicks + 3));
            } else if (currentTicks > targetTicksFrozen) {
                player.setTicksFrozen(Math.max(targetTicksFrozen, currentTicks - 3));
            }
        } 
        else if (!inSnow && currentTicks > 0) {
            player.setTicksFrozen(Math.max(0, currentTicks - 2));
        }
    }


    private static void removeColdOverlay(ServerPlayer player) {
        player.setTicksFrozen(0);
    }

    private static void showHotOverlay(ServerPlayer player) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(60, 100000, 60));
        player.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(" ")));
        player.connection.send(new ClientboundSetTitleTextPacket(
            Component.literal(" \uE787").withStyle(ChatFormatting.WHITE)));

        ACTIVE_HOT_OVERLAYS.add(player.getUUID());
    }

    private static void removeHotOverlay(ServerPlayer player) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 0, 20));
        player.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(" ")));
        player.connection.send(new ClientboundSetTitleTextPacket(
            Component.literal(" \uE787").withStyle(ChatFormatting.WHITE)));

        ACTIVE_HOT_OVERLAYS.remove(player.getUUID());
    }

    private static boolean shouldShowColdOverlay(Player player) {
        if(player.gameMode().isCreative()) return false;

        float playerTemperature = TemperatureManager.getPlayerTemperature(player);
        
        return playerTemperature <= TTNTuningConfig.MIN_TEMP_FOR_NO_OVERLAY;
    }

    private static boolean shouldShowHotOverlay(Player player) {
        if(player.gameMode().isCreative()) return false;



        float playerTemperature = TemperatureManager.getPlayerTemperature(player);


        return playerTemperature >= TTNTuningConfig.MAX_TEMP_FOR_NO_OVERLAY;
    }

}
