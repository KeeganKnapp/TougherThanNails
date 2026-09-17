package dev.keeganknapp.tougherthannails;


import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class EffectManager {

    private static int tickCounter = 0;

    public static void onServerTick(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();


        //fire once every second
        tickCounter++;

        for(ServerPlayer player : players) {

            if (tickCounter % 40 == 0) {
                if(shouldTakeThirstDamage(player)) {
                    damagePlayerThirst(player); 
                }
                else if(shouldTakeTemperatureDamage(player)) {
                    damagePlayerTemperature(player);
                }
            }
            
            if(shouldPreventSprinting(player)) {
                preventSprinting(player);
            }
        }

    }


    private static void damagePlayerThirst(Player player) {
        /*
        DamageScaling damageScaling = DamageScaling.ALWAYS;
        ResourceKey<DamageType> damageTypeKey = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath("tougherthannails", "thirst")
        );

        RegistryAccess registryAccess = player.level().registryAccess();

        Holder<DamageType> damageHolder = registryAccess
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .getOrThrow(damageTypeKey);

        DamageSource damageSource = new DamageSource(damageHolder);

        player.hurtServer((ServerLevel)player.level(), damageSource, 1.0f); 
        */


        ServerLevel serverLevel = (ServerLevel) player.level();

        player.hurtServer(serverLevel, serverLevel.damageSources().generic(), TTNTuningConfig.THIRST_DAMAGE_AMT);
    }

    private static void damagePlayerTemperature(Player player) {
        /*
        DamageScaling damageScaling = DamageScaling.ALWAYS;
        ResourceKey<DamageType> damageTypeKey = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath("tougherthannails", "temperature")
        );

        RegistryAccess registryAccess = player.level().registryAccess();

        Holder<DamageType> damageHolder = registryAccess
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .getOrThrow(damageTypeKey);

        DamageSource damageSource = new DamageSource(damageHolder);

        player.hurtServer((ServerLevel)player.level(), damageSource, 1.0f); 
        */

        ServerLevel serverLevel = (ServerLevel) player.level();

        player.hurtServer(serverLevel, serverLevel.damageSources().generic(), TTNTuningConfig.TEMPERATURE_DAMAGE_AMT);
    }

    private static void preventSprinting(ServerPlayer player) {
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) return;

        boolean sprinting  = player.isSprinting();
        boolean hasModifier = speed.hasModifier(ModModifiers.ANTI_SPRINT_ID);

        if (sprinting && !hasModifier) {
            speed.addTransientModifier(ModModifiers.CANCEL_SPRINT_SPEED);
        } else if (!sprinting && hasModifier) {
            speed.removeModifier(ModModifiers.ANTI_SPRINT_ID);
        }   
    } 

    private static boolean shouldTakeThirstDamage(Player player) {
        int playerThirst = ThirstManager.getPlayerThirst(player);
        return playerThirst <= TTNTuningConfig.MIN_THIRST_FOR_DAMAGE;
    }

    private static boolean shouldTakeTemperatureDamage(Player player) {
        float playerTemp = TemperatureManager.getPlayerTemperature(player);
        return playerTemp >= TTNTuningConfig.MAX_TEMP_FOR_NO_DAMAGE || playerTemp <= TTNTuningConfig.MIN_TEMP_FOR_NO_DAMAGE;
    }

    private static boolean shouldPreventSprinting(Player player) {
        float playerTemp = TemperatureManager.getPlayerTemperature(player);
        int playerThirst = ThirstManager.getPlayerThirst(player);
        return playerThirst <= TTNTuningConfig.MAX_THIRST_FOR_NO_SPRINT || playerTemp >= TTNTuningConfig.MAX_TEMP_FOR_NO_SPRINT;
    }

}
