package dev.keeganknapp.tougherthannails;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;

import net.fabricmc.loader.api.FabricLoader;

public class TTNTuningConfig {
    
        public int test = 0;

        public static final int MIN_TEMP_FOR_NO_OVERLAY = 6;
        public static final int MAX_TEMP_FOR_NO_OVERLAY = 23;

        public static final int MIN_TEMP_FOR_NO_SPRINT = 6;
        public static final int MAX_TEMP_FOR_NO_SPRINT = 22;

        public static final int MIN_TEMP_FOR_NO_DAMAGE = 0;
        public static final int MAX_TEMP_FOR_NO_DAMAGE = 25;

        public static final int MAX_THIRST_FOR_NO_SPRINT = 6;
        public static final int MIN_THIRST_FOR_DAMAGE = 0;

        public static final float TEMPERATURE_DAMAGE_AMT = 1.0f;
        public static final float THIRST_DAMAGE_AMT = 1.0f;

        public static final float METERS_SPRINTED_BODY_HEAT = 0.07F;
        public static final float METERS_SWAM_BODY_HEAT = 0.03F;
        public static final float BLOCK_BROKEN_BODY_HEAT = 0.02F;
        public static final float DAMAGE_TAKEN_BODY_HEAT = 0.04F;
        public static final float DAMAGE_DELT_BODY_HEAT = 0.03F;
        public static final float JUMP_BODY_HEAT = 0.03F;

        public static final float BODY_HEAT_REDUCTION_RATE = 0.1F;

        public static final float POTION_THIRST = 3;
        public static final float POTION_SATURATION = 0.5F;

        public static final float MILK_BUCKET_THIRST = 8;
        public static final float MILK_BUCKET_SATURATION = 0.8F;

        public static final float WATER_BUCKET_THIRST = 10;
        public static final float WATER_BUCKET_SATURATION = 0.1F;

        public static final int BLOCK_SEARCH_RADIUS = 7;

        public static final float SWEAT_COOLING_RATE = 0.15F;
        public static final float SWEAT_WATER_COST   = 1.0F;

        public static final float IN_COLD_BLOCK_MODIFIER = -4.0F;

        public static final float WATER_BLOCK_THIRST = 1.0F;
        public static final float WATER_BLOCK_SATURATION = 0.0F;

        public static final float DEHYDRATION_THIRST_DELTA_MULTIPLIER = 1.5F;

        public static final float CHANCE_OF_DEHYDRATION_FROM_DIRTY_WATER = 0.1F;

        public static final float SNOW_TEMPERATURE_MODIFIER = -4.0F;
        public static final float RAIN_TEMPERATURE_MODIFIER = -3.0F;

	public static TTNTuningConfig load() {
		Path path = FabricLoader.getInstance().getConfigDir().resolve("wendigo-tuning.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				TTNTuningConfig loaded = gson.fromJson(reader, TTNTuningConfig.class);
				return loaded != null ? loaded : new TTNTuningConfig();
			} catch (IOException e) {
				throw new RuntimeException("Failed to read " + path, e);
			}
		}

		TTNTuningConfig defaults = new TTNTuningConfig();
		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, gson.toJson(defaults));
		} catch (IOException e) {
			throw new RuntimeException("Failed to write default config to " + path, e);
		}
		return defaults;
	}
}
