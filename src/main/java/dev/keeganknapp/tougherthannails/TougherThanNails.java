package dev.keeganknapp.tougherthannails;

import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

import org.slf4j.Logger;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;


/**
 * Entry point for the MusicSync mod: an autohosted, server-side-only reimplementation of the
 * old MusicSyncRP/MusicSyncDP loose resource pack + datapack pair. Polymer's
 * polymer-resource-pack + polymer-autohost merge this mod's bundled assets/ folder into a
 * single generated resource pack and automatically host/push it to every joining vanilla
 * client - no manual zipping/uploading, and no client-side mod required.
 */
public final class TougherThanNails implements ModInitializer {

    public static final String MOD_ID = "tougherthannails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TTNTuningConfig tuningConfig;

    public static final ResourceKey<Registry<HeatSourceConfig>> HEAT_SOURCE_REGISTRY_KEY =
        ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "heat_sources"));

    public static final Identifier FIRE_RESISTANCE_HEAT_RESISTANCE = Identifier.fromNamespaceAndPath("tougherthannails", "fire_resistance_heat_resistance");




    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        // Create the logger instance


        DynamicRegistries.register(HEAT_SOURCE_REGISTRY_KEY, HeatSourceConfig.CODEC);

        ModAttributes.initialize();
        ModStatusEffects.register();
        ModPotions.register();
        ModItems.init();
        ThirstManager.init();
        TemperatureManager.init();
        TTNTuningConfig.load();


        PolymerResourcePackUtils.addModAssets(MOD_ID);
        PolymerResourcePackUtils.markAsRequired(); // optional


        //on tick events
        ServerTickEvents.END_SERVER_TICK.register(ActionBarManager::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(ThirstManager::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(TemperatureManager::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(EffectManager::onServerTick);


        var fireResistanceEffect = MobEffects.FIRE_RESISTANCE.value();

        fireResistanceEffect.addAttributeModifier(
            ModAttributes.HEAT_RESISTANCE, 
            FIRE_RESISTANCE_HEAT_RESISTANCE,
            7,
            AttributeModifier.Operation.ADD_VALUE
        );

        DefaultItemComponentEvents.MODIFY.register(context ->
            context.modify(
                Predicate.isEqual(Items.PRISMARINE_SHARD),
            (DataComponentMap.Builder builder, HolderLookup.Provider lookup, Item item) ->
                builder.set(
                    DataComponents.PROVIDES_TRIM_MATERIAL,
                    lookup.lookupOrThrow(Registries.TRIM_MATERIAL)
                    .getOrThrow(ModArmorTrimMaterials.CUSTOM_MATERIAL_KEY)   // Holder<TrimMaterial>
                )
            ));
    }


}
