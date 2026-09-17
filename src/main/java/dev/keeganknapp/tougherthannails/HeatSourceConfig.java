package dev.keeganknapp.tougherthannails;


import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record HeatSourceConfig(float heatValue) {
    
    // The exact Codec parsing strategy used to map raw JSON numbers directly to this Record
    public static final Codec<HeatSourceConfig> CODEC = Codec.FLOAT.xmap(
        HeatSourceConfig::new,
        HeatSourceConfig::heatValue
    );

    /**
     * Helper lookup method to check if a block in the world has a heat config attached.
     * Use this anywhere you have access to a Level and a BlockState.
     */
    public static float getHeatFromWorld(Level level, BlockState state, ResourceKey<Registry<HeatSourceConfig>> registryKey) {
        try {
            // Obtains the dynamic registry safe wrapper
            return level.registryAccess().lookupOrThrow(registryKey)
            .get(ResourceKey.create(registryKey, BuiltInRegistries.BLOCK.getKey(state.getBlock())))
            .map(holder -> holder.value().heatValue()) // holder.value() unwraps the config
            .orElse(0.0f);
        } catch (Exception e) {
            return 0.0f; // Falls back safely if the world is still loading
        }
    }
}
