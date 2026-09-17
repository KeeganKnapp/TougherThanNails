package dev.keeganknapp.tougherthannails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import dev.keeganknapp.tougherthannails.TougherThanNails;
import dev.keeganknapp.tougherthannails.components.FloatComponent;

public class TemperatureManager {

    private static final float MIN_BIOME_TEMP = -0.7f;
    private static final float MAX_BIOME_TEMP = 2.0f;

    private static int tickCounter = 0;

    private static Map<Player, Float> totalMetersSprintedMap = new HashMap<>();
    private static Map<Player, Float> totalMetersSwamMap = new HashMap<>();
    private static Map<Player, Integer> totalJumpsMap = new HashMap<>();

    public static void init() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> { 
                increaseBodyHeat(player, TTNTuningConfig.BLOCK_BROKEN_BODY_HEAT); });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, dealtDamage, blocked) -> {
                if (entity instanceof Player p) {
                    increaseBodyHeat(p, TTNTuningConfig.DAMAGE_TAKEN_BODY_HEAT);
                } });

        // when damage taken
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, dealtDamage, blocked) -> {
                if (source.getEntity() instanceof Player p) {
                    increaseBodyHeat(p, TTNTuningConfig.DAMAGE_DELT_BODY_HEAT);
                } });
    }



    public static void onServerTick(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        //fire once every second
        tickCounter++;
        if (tickCounter % 20 == 0) {

            for(ServerPlayer player : players) {

                if(player.gameMode().isCreative()) continue;

                lerpToTargetTemp(player);


                increaseBodyHeat(player, getDeltaMetersSprinted(player) * TTNTuningConfig.METERS_SPRINTED_BODY_HEAT);
                increaseBodyHeat(player, getDeltaMetersSwam(player) * TTNTuningConfig.METERS_SWAM_BODY_HEAT);
                increaseBodyHeat(player, getDeltaJumps(player) * TTNTuningConfig.JUMP_BODY_HEAT);

                //decreaseBodyHeat(player, TTNTuningConfig.BODY_HEAT_REDUCTION_RATE); 
                
                applySweatCooling(player); } } }



    public static void lerpToTargetTemp(ServerPlayer player) {
        float playerTemp = ModComponents.TEMPERATURE.get(player).getValue();
        float targetTemp = getTargetTemp(player);

        float step = stepToward(targetTemp, playerTemp);

        ModComponents.TEMPERATURE.get(player).increment(step); 
    }

    public static float stepToward(float target, float current) {
        float distance = target - current;

        if(Math.abs(distance) < 0.0001f) {
            return 0.0f;
        }

        float speed = Math.min(Math.abs(distance), 12f);
        float stepSize = 1.0f / (13.0f - speed);

        float deltaTemp = stepSize * Math.signum(distance);

        if(Math.abs(deltaTemp) > Math.abs(distance)) {
            deltaTemp = distance;
        }

        return deltaTemp;
    }


    private static int getTargetTemp(ServerPlayer player) {
        return Math.round(
            applyResistance(player, getEnvironmentalTemp(player) + getBodyHeatRaw(player))
        );
    }

    public static float getEnvironmentalTemp(ServerPlayer player) {
        float biomeTempModifier = getBiomeTempModifier(player);
        float timeOfDayMultipler = getTimeOfDayMultiplier(player);
        float altitudeModifier = getAltitudeTemperatureModifer(player);
        float armorModifier = getArmorModifiers(player);
        float bodyHeatModifier = getBodyHeatRaw(player);
        float nearbyBlocksModifier = getNearbyBlocksModifier(player);
        float inColdBlockModifier = getInColdBlockModifier(player);
        float precipitationModifier = getWeatherModifier(player);

        var playerManager = player.level().getServer().getPlayerList();
        playerManager.broadcastSystemMessage(Component.literal("biome temp modifier: " + biomeTempModifier), false);
        playerManager.broadcastSystemMessage(Component.literal("armor modifiers: " + armorModifier), false);
        playerManager.broadcastSystemMessage(Component.literal("altitude modifier: " + altitudeModifier ), false);
        playerManager.broadcastSystemMessage(Component.literal("bodyheat modifiers: " + bodyHeatModifier ), false);
        playerManager.broadcastSystemMessage(Component.literal("nearby blocks modifiers: " + nearbyBlocksModifier), false);
        playerManager.broadcastSystemMessage(Component.literal("time of day multiplier: " + timeOfDayMultipler), false);
        playerManager.broadcastSystemMessage(Component.literal("in cold block multiplier: " + timeOfDayMultipler), false);
        playerManager.broadcastSystemMessage(Component.literal("weather modifier: " + precipitationModifier), false);


        float environmentalTemp =
        biomeTempModifier * timeOfDayMultipler
        + altitudeModifier
        + armorModifier
        + nearbyBlocksModifier
        + inColdBlockModifier
        + precipitationModifier;

        playerManager.broadcastSystemMessage(Component.literal("environmental temp: " + environmentalTemp +" \n"), false);

        return environmentalTemp;
    }

    private static float applyResistance(ServerPlayer player, float targetTemp) {
        double heatResistance = 0.0f;
        double coldResistance = 0.0f;
        
        if(player.getAttributes().hasAttribute(ModAttributes.COLD_RESISTANCE))
            coldResistance = player.getAttributeValue(ModAttributes.COLD_RESISTANCE);
        if(player.getAttributes().hasAttribute(ModAttributes.HEAT_RESISTANCE))
            heatResistance = player.getAttributeValue(ModAttributes.HEAT_RESISTANCE);
        
        return clampFromResistance(targetTemp, heatResistance, coldResistance);
    }

    public static float clampFromResistance(float targetTemp, double heatResistance, double coldResistance) {
        return Math.clamp(targetTemp, 0.0F + (float) coldResistance, 25.0F - (float) heatResistance);
    }

    private static int getArmorModifiers(ServerPlayer player) {
        double cooling = 0.0;
        double heating = 0.0;

        if(player.getAttributes().hasAttribute(ModAttributes.COOLING))
            cooling = player.getAttributeValue(ModAttributes.COOLING);
        if(player.getAttributes().hasAttribute(ModAttributes.HEATING))
            heating = player.getAttributeValue(ModAttributes.HEATING);

        return (int) (heating - cooling); }

    private static float getAltitudeTemperatureModifer(ServerPlayer player) {
        ResourceKey<Level> currentDimension = player.level().dimension();

        if(currentDimension != Level.OVERWORLD)
            return 0;

        float yPos = (float) player.position().y;
        return -(Math.abs(( yPos - 64 ) / 21 )); }

    private static float getBiomeTempModifier(ServerPlayer player) {
        Biome biome = ((ServerLevel)player.level()).getBiome(player.blockPosition()).value();
        float temp = getTemperatureOfBiome(biome);
        dev.keeganknapp.tougherthannails.TougherThanNails.LOGGER.info("Scaled temperature of player : " + temp);
        return temp; }

    // Uses biomes moisture (vegetation) + daytime to simulate nighttime temp drops. Less moist biomes trap less heat and thus lose more at night
    // Just like real life
    // Desert = 0.0 precip = 15% colder
    //
    private static float getTimeOfDayMultiplier(ServerPlayer player) {
        ResourceKey<Level> currentDimension = player.level().dimension();

        if(currentDimension != Level.OVERWORLD)
            return 1.0F;

        ServerLevel level = (ServerLevel) player.level();
        Biome biome = level.getBiome(player.blockPosition()).value();
        float biomeHumidity = biome.climateSettings.downfall(); 

        long dayTime = level.getOverworldClockTime() % 24000L;

        float frac = Math.floorMod(dayTime - 6000L, 24000L) / 24000f;
        float nightFactor = (1f - (float) Math.cos(frac * 2 * Math.PI)) / 2f;

        float nightFloor = 0.13f * biomeHumidity + 0.85f;
        return 1.0f + nightFactor * (nightFloor - 1.0f);
    }


    private static float getInColdBlockModifier(ServerPlayer player) {
        BlockPos feetPos = BlockPos.containing(player.position());
        BlockPos eyePos = BlockPos.containing(player.getEyePosition());

        boolean inColdBlock = 
            player.level().getBlockState(feetPos).is(Blocks.POWDER_SNOW) 
            || player.level().getBlockState(eyePos).is(Blocks.POWDER_SNOW)
            || player.level().getBlockState(feetPos).is(Blocks.WATER) 
            || player.level().getBlockState(eyePos).is(Blocks.WATER);

        if(inColdBlock) {
            return TTNTuningConfig.IN_COLD_BLOCK_MODIFIER;
        }
        else
            return 0;
    }



    private static float getNearbyBlocksModifier(ServerPlayer player) {
        return getHottestNearbyBlock(player);
    }

    public static float getTemperatureOfBiome(Biome biome) {
        float biomeTemp = biome.getBaseTemperature();
        dev.keeganknapp.tougherthannails.TougherThanNails.LOGGER.info("Actual temperature of biome : " + biomeTemp);
        return ((biomeTemp - MIN_BIOME_TEMP) * (25)) / (MAX_BIOME_TEMP - MIN_BIOME_TEMP); }

    public static float getWeatherModifier(ServerPlayer player) {
        Level level = player.level();
        Biome biome = ((ServerLevel)player.level()).getBiome(player.blockPosition()).value();
        Biome.Precipitation precipitation = biome.getPrecipitationAt(player.blockPosition(), level.getSeaLevel());

        if(precipitation == Biome.Precipitation.SNOW) {
            return TTNTuningConfig.SNOW_TEMPERATURE_MODIFIER;
        }
        else if(precipitation == Biome.Precipitation.RAIN) {
            return TTNTuningConfig.RAIN_TEMPERATURE_MODIFIER;
        }

        return 0.0f;
    }



    public static int getPlayerTemperature(Player player) {
        return Math.round(getTemperatureRaw(player)); }

    public static int getPlayerBodyHeat(Player player) {
        return Math.round(getBodyHeatRaw(player)); }



    public static float getTemperatureRaw(Player player) {
        return ModComponents.TEMPERATURE.get(player).getValue(); }

    public static float getBodyHeatRaw(Player player) {
        return ModComponents.BODY_HEAT.get(player).getValue(); }

    private static void increaseBodyHeat(Player player, float amt) {
        ModComponents.BODY_HEAT.get(player).increment(amt); }

    private static void decreaseBodyHeat(Player player, float amt) {
        ModComponents.BODY_HEAT.get(player).increment(-amt); }


    private static float getDeltaMetersSprinted(ServerPlayer player) {
        float last = totalMetersSprintedMap.computeIfAbsent(player, k -> 0.0F);
        float current = player.getStats().getValue(Stats.CUSTOM.get(Stats.SPRINT_ONE_CM))/100; 

        float delta = current-last;
        totalMetersSprintedMap.put(player, current);

        return delta; }
    private static float getDeltaMetersSwam(ServerPlayer player) {
        float last = totalMetersSwamMap.computeIfAbsent(player, k -> 0.0F);
        float current = player.getStats().getValue(Stats.CUSTOM.get(Stats.SWIM_ONE_CM))/100; 

        float delta = current-last;
        totalMetersSwamMap.put(player, current);

        return delta; }
    private static int getDeltaJumps(ServerPlayer player) {
        int last = totalJumpsMap.computeIfAbsent(player, k -> 0);
        int current = player.getStats().getValue(Stats.CUSTOM.get(Stats.JUMP)); 

        int delta = current-last;
        totalJumpsMap.put(player, current);

        return delta; }



    private static float getHottestNearbyBlock(ServerPlayer player) {
        ServerLevel level = player.level();
        BlockPos center = player.blockPosition();

        int minX = center.getX() - TTNTuningConfig.BLOCK_SEARCH_RADIUS;
        int minY = center.getY() - TTNTuningConfig.BLOCK_SEARCH_RADIUS;
        int minZ = center.getZ() - TTNTuningConfig.BLOCK_SEARCH_RADIUS;
        int maxX = center.getX() + TTNTuningConfig.BLOCK_SEARCH_RADIUS;
        int maxY = center.getY() + TTNTuningConfig.BLOCK_SEARCH_RADIUS;
        int maxZ = center.getZ() + TTNTuningConfig.BLOCK_SEARCH_RADIUS; 

        float maxHeat = 0;
        BlockPos maxBlockPos = new BlockPos(0,0,0);

        for (BlockPos mutablePos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {

            BlockPos pos = mutablePos.immutable();

            BlockState state = level.getBlockState(pos);


            float currentHeat = HeatSourceConfig.getHeatFromWorld(player.level(), state, TougherThanNails.HEAT_SOURCE_REGISTRY_KEY);

            if(currentHeat > maxHeat) {
                maxHeat = currentHeat;
                maxBlockPos = pos;
            }

        }

        double distSq = Math.sqrt(player.distanceToSqr(maxBlockPos.getCenter()));


        return (float) ((1 - Math.max(0, Math.pow(distSq / TTNTuningConfig.BLOCK_SEARCH_RADIUS, 2))) * maxHeat);

    }

    private static void applySweatCooling(ServerPlayer player) {
        FloatComponent bodyHeat = ModComponents.BODY_HEAT.get(player);
        FloatComponent sat      = ModComponents.THIRST_SATURATION.get(player);
        float heat = bodyHeat.getValue();
        if (heat <= 0f || sat.getValue() <= 0f) return;   // no sweat when dry — that's the punishment state

        float shed = Math.min(heat, TTNTuningConfig.SWEAT_COOLING_RATE);
        bodyHeat.decrement(shed);
        ThirstManager.reduceThirst(player, shed * TTNTuningConfig.SWEAT_WATER_COST); // burns saturation first, then thirst
    }
}

