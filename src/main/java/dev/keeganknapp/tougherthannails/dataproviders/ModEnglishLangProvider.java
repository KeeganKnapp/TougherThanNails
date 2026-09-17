package dev.keeganknapp.tougherthannails.dataproviders;

import java.util.concurrent.CompletableFuture;

import dev.keeganknapp.tougherthannails.ModAttributes;
import dev.keeganknapp.tougherthannails.ModItems;
import dev.keeganknapp.tougherthannails.ModStatusEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.alchemy.Potion;

public class ModEnglishLangProvider extends FabricLanguageProvider {
    public ModEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        // Enforce the 'en_us' locale
        super(dataOutput, "en_us", registryLookup);
    }



    @Override
    public void generateTranslations(Provider registryLookup, TranslationBuilder tb) {
        tb.add(ModStatusEffects.COLD_RESISTANCE_HOLDER.value(), "Cold Resistance");
        tb.add(ModStatusEffects.HEAT_RESISTANCE_HOLDER.value(), "Heat Resistance");
        tb.add(ModStatusEffects.DEHYDRATION_HOLDER.value(),     "Dehydration");

        tb.add(ModAttributes.HEATING, "Heating");
        tb.add(ModAttributes.COOLING, "Cooling");

        tb.add(ModItems.COLD_RESISTANCE_POTION, "Potion of Cold Resistance");
        tb.add(ModItems.HEAT_RESISTANCE_POTION, "Potion of Heat Resistance");

        tb.add(ModItems. FUR_HIDE, "Fur Hide");
        tb.add(ModItems.FUR_HIDE_HELMET, "Fur Hide Hood");
        tb.add(ModItems.FUR_HIDE_CHESTPLATE, "Fur Hide Coat");
        tb.add(ModItems.FUR_HIDE_LEGGINGS, "Fur Hide Pants");
        tb.add(ModItems.FUR_HIDE_BOOTS, "Fur Hide Boots");

        tb.add(ModItems.PRISMARINE_HELMET, "Prismarine Helmet");
        tb.add(ModItems.PRISMARINE_CHESTPLATE, "Prismarine Chestplate");
        tb.add(ModItems.PRISMARINE_LEGGINGS, "Prismarine Leggings");
        tb.add(ModItems.PRISMARINE_BOOTS, "Prismarine Boots");

        tb.add(ModItems.APPLE_JUICE, "Apple Juice");
        tb.add(ModItems.CARROT_JUICE, "Carrot Juice");
        tb.add(ModItems.CHORUS_FRUIT_SMOOTHIE, "Chorus Fruit Smoothie");
        tb.add(ModItems.SWEET_BERRY_JUICE, "Sweet Berry Juice");
        tb.add(ModItems.GLOW_BERRY_JUICE, "Glow Berry Juice");
        tb.add(ModItems.HOT_COCOA, "Hot Cocoa");
        tb.add(ModItems.BONE_BROTH, "Bone Broth");
        tb.add(ModItems.PURIFIED_WATER_BOTTLE, "Purified Water Bottle");


        tb.add(ModItems.WATER_BUCKET_DRINKABLE, "Water Bucket");


        tb.add("trim_material.tougherthannails.prismarine", "Prismarine");




    }
}
