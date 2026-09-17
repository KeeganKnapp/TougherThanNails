package dev.keeganknapp.tougherthannails;

import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public class ModArmorTrimMaterials {
    public static final ResourceKey<TrimMaterial> CUSTOM_MATERIAL_KEY = ResourceKey.create(
        Registries.TRIM_MATERIAL,
        Identifier.fromNamespaceAndPath("tougherthannails", "prismarine_trim_material")
    );

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(CUSTOM_MATERIAL_KEY, Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PRISMARINE_SHARD, false, context);
    }


    private static void register(ResourceKey<TrimMaterial> key, Item templateItem, Item materialItem, boolean decal, BootstrapContext<TrimMaterial> context) {

        MaterialAssetGroup assetGroup = MaterialAssetGroup.create("prismarine");


        // Construct the vanilla record
        TrimMaterial material = new TrimMaterial(
            assetGroup,                        
            Component.translatable("Prismarine Trim")
        );

        context.register(CUSTOM_MATERIAL_KEY, material);
    }
}


