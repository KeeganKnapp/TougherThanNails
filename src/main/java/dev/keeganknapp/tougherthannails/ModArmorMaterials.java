package dev.keeganknapp.tougherthannails;


import java.util.EnumMap;
import java.util.Map;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class ModArmorMaterials {
    // -> assets/tougherthannails/equipment/fur_hide.json in the generated pack
    public static final ResourceKey<EquipmentAsset> FUR_HIDE_ASSET =
    ResourceKey.create(EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, "fur_hide"));

    // -> assets/tougherthannails/equipment/prismarine.json in the generated pack
    public static final ResourceKey<EquipmentAsset> PRISMARINE_ASSET =
    ResourceKey.create(EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, "prismarine"));

    private static Map<ArmorType, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        return Util.make(new EnumMap<>(ArmorType.class), map -> {
            map.put(ArmorType.BOOTS, boots);
            map.put(ArmorType.LEGGINGS, leggings);
            map.put(ArmorType.CHESTPLATE, chestplate);
            map.put(ArmorType.HELMET, helmet);
        });
    }

    public static final ArmorMaterial FUR_HIDE = new ArmorMaterial(
        15,
        defense(2, 5, 6, 2),
        9,                                      // enchantability
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0F, 0.0F,
        ItemTags.REPAIRS_LEATHER_ARMOR,
        FUR_HIDE_ASSET
    );

    public static final ArmorMaterial PRISMARINE = new ArmorMaterial(
        25,
        defense(2, 5, 6, 2),
        10,                                     // enchantability
        SoundEvents.ARMOR_EQUIP_IRON,
        1.0F, 0.0F,
        ItemTags.REPAIRS_LEATHER_ARMOR,
        PRISMARINE_ASSET
    );
}
