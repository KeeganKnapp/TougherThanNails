package dev.tougherthannails.gametest;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;

import dev.keeganknapp.tougherthannails.ModComponents;
import dev.keeganknapp.tougherthannails.ModItems;
import dev.keeganknapp.tougherthannails.TTNTuningConfig;
import dev.keeganknapp.tougherthannails.TemperatureManager;
import dev.keeganknapp.tougherthannails.TougherThanNails;
import io.netty.channel.embedded.EmbeddedChannel;

import net.fabricmc.fabric.api.gametest.v1.GameTest;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class TTNGameTest {
	private static ServerPlayer makeSurvivalMockPlayer(GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "test-mock-player");
		CommonListenerCookie cookie = CommonListenerCookie.createInitial(profile, false);
		ServerPlayer player = new ServerPlayer(level.getServer(), level, profile, ClientInformation.createDefault());
		Connection connection = new Connection(PacketFlow.SERVERBOUND);
		new EmbeddedChannel(connection);
		level.getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
		player.setGameMode(GameType.SURVIVAL);
		return player;
	}

        /*
        @GameTest
	public void noThirstDamagesPlayer(GameTestHelper helper) {
		WendigoEntity wendigo = helper.spawn(ModEntities.WENDIGO, new BlockPos(1, 1, 1));
		helper.assertTrue(wendigo.getMaxHealth() == 50.0F,
			"expected 50 max health, got " + wendigo.getMaxHealth());
		helper.succeed();
	}
        */


        @GameTest
        // assert that for a given biome the scaled temperature score we calculate will be accurate
        // the total range is 2.7, so we will normalize to 0.0 - 2.0 linearly then multiply by 12.
        // FUTURE: might also use exponential to make colder biomes more cold, but we'll see
        //
        // frozen peaks should return a value of 12 * 0.0
        // badlands should return a value of 12 * 2.0
        // values above  
    public void scaledBiomeTemperatureScoreMatchesBiome(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Holder<Biome> frozenPeaks = level.registryAccess()
        .lookupOrThrow(Registries.BIOME)
        .getOrThrow(Biomes.FROZEN_PEAKS);

        float score = TemperatureManager.getTemperatureOfBiome(frozenPeaks.value());
        helper.assertTrue(score == 0.0f, "expected 0.0 for Frozen Peaks, got " + score);
        helper.succeed();

    }

    @GameTest(maxTicks = 100)
    public void zombiesSpawnWithFurHideArmorInColdBiomes(GameTestHelper helper) {
        helper.getLevel().getServer().setDifficulty(Difficulty.HARD, true);

        helper.getLevel().getChunkAt(helper.absolutePos(new BlockPos(0,0,0) )).setInhabitedTime(3600000L);
        helper.setBiome(Biomes.SNOWY_TAIGA);

        helper.onEachTick(() -> {
            Zombie entity = helper.spawn(EntityType.ZOMBIE, 0, 0, 0);
            TougherThanNails.LOGGER.info("Spawned zombie at test 0,0,0");

            BlockPos absoluteZombiePos = entity.blockPosition();
            var level = helper.getLevel();

            level.getChunkAt(absoluteZombiePos).setInhabitedTime(3600000L);

            entity.finalizeSpawn(level, level.getCurrentDifficultyAt(absoluteZombiePos), EntitySpawnReason.STRUCTURE, null);

            TougherThanNails.LOGGER.info("Entity biome: " + helper.getLevel().getBiome(helper.absolutePos(entity.blockPosition())).getRegisteredName());


            ItemStack feetItem = entity.getItemBySlot(EquipmentSlot.FEET);
            ItemStack legsItem = entity.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);

            TougherThanNails.LOGGER.info("Feet Item: " + feetItem.toString());
            TougherThanNails.LOGGER.info("Chest Item: " + chestItem.toString());
            TougherThanNails.LOGGER.info("Leggings Item: " + legsItem.toString());
            TougherThanNails.LOGGER.info("Helmet Item: " + headItem.toString());

            if (feetItem.is(ModItems.FUR_HIDE_BOOTS)
            || legsItem.is(ModItems.FUR_HIDE_LEGGINGS) 
            || chestItem.is(ModItems.FUR_HIDE_CHESTPLATE)
            || headItem.is(ModItems.FUR_HIDE_HELMET)
        ) {
                helper.succeed();
                return; 
            }

            entity.discard(); 
        });
    }

    /* 
     * The most biggest step size we can get in here is 1.0f at a time. We take the the largest between the distance between target and current and use
     * the min between that and 12.0F, then normalize, so a distance of >12.0 will be 1.0f step size
     */

    @GameTest
    public void temperatureValueStepsTowardTargetVariably(GameTestHelper helper) {
        float targetTemp = 25;
        float currentTemp = 0;

        float deltaTemp = TemperatureManager.stepToward(targetTemp, currentTemp);

        helper.assertTrue(deltaTemp == 1, "temperature did not correctly step toward target. Actual delta: " + deltaTemp + " ");

        targetTemp = 12;
        currentTemp = 12;

        deltaTemp = TemperatureManager.stepToward(targetTemp, currentTemp);

        helper.assertTrue(deltaTemp == 0, "temperature did not correctly step toward target. Actual delta: " + deltaTemp + " ");

        targetTemp = 13;
        currentTemp = 12;

        deltaTemp = TemperatureManager.stepToward(targetTemp, currentTemp);

        helper.assertTrue(deltaTemp == 1.0f/12.0f, "temperature did not step correctly toward target. Actual delta: " + deltaTemp + " ");
        helper.succeed();

    }

    @GameTest
    public void clampFromResistanceWorks(GameTestHelper helper) {
        double coldResistance = 0.0;
        double heatResistance = 0.0;

        float targetTemp = 12.0f;

        helper.assertTrue(TemperatureManager.clampFromResistance(targetTemp, coldResistance, heatResistance) == 12.0f, "Resistance did not succesfully clamp temperature");

        coldResistance = 7.0f;
        heatResistance = 7.0f;

        targetTemp = 25.0f;

        helper.assertTrue(TemperatureManager.clampFromResistance(targetTemp, coldResistance, heatResistance) == 25.0f - heatResistance, "Resistance did not succesfully clamp temperature");

        targetTemp = 0.0f;

        helper.assertTrue(TemperatureManager.clampFromResistance(targetTemp, coldResistance, heatResistance) == 0.0f + coldResistance, "Resistance did not succesfully clamp temperature");

        helper.succeed();

    }


}
