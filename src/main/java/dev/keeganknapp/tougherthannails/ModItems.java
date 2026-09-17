package dev.keeganknapp.tougherthannails;


import java.util.function.BiFunction;
import java.util.function.Function;

import dev.keeganknapp.tougherthannails.items.DrinkableWaterBucket;
import dev.keeganknapp.tougherthannails.items.ModDrinkItem;
import dev.keeganknapp.tougherthannails.items.ModPotionItem;
import dev.keeganknapp.tougherthannails.items.PolymerArmorItem;
import dev.keeganknapp.tougherthannails.items.PolymerMaterialItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;


public final class ModItems {


    public static void init() {

    }

    //crafting materials 
    public static final Item FUR_HIDE = register("fur_hide",
        key -> new PolymerMaterialItem(new Item.Properties().setId(key), Items.LEATHER));

    // fur hide armor
    public static final PolymerArmorItem FUR_HIDE_HELMET = registerArmor(
        "fur_hide_helmet", Items.LEATHER_HELMET, ArmorType.HELMET,
        ModArmorMaterials.FUR_HIDE, ModAttributes.HEATING, 1.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem FUR_HIDE_CHESTPLATE = registerArmor(
        "fur_hide_chestplate", Items.LEATHER_CHESTPLATE, ArmorType.CHESTPLATE,
        ModArmorMaterials.FUR_HIDE, ModAttributes.HEATING, 2.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem FUR_HIDE_LEGGINGS = registerArmor(
        "fur_hide_leggings", Items.LEATHER_LEGGINGS, ArmorType.LEGGINGS,
        ModArmorMaterials.FUR_HIDE, ModAttributes.HEATING, 2.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem FUR_HIDE_BOOTS = registerArmor(
        "fur_hide_boots", Items.LEATHER_BOOTS, ArmorType.BOOTS,
        ModArmorMaterials.FUR_HIDE, ModAttributes.HEATING, 1.0,
        PolymerArmorItem::new);

    // prismarine armor 
    public static final PolymerArmorItem PRISMARINE_HELMET = registerArmor(
        "prismarine_helmet", Items.LEATHER_HELMET, ArmorType.HELMET,
        ModArmorMaterials.PRISMARINE, ModAttributes.COOLING, 1.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem PRISMARINE_CHESTPLATE = registerArmor(
        "prismarine_chestplate", Items.LEATHER_CHESTPLATE, ArmorType.CHESTPLATE,
        ModArmorMaterials.PRISMARINE, ModAttributes.COOLING, 2.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem PRISMARINE_LEGGINGS = registerArmor(
        "prismarine_leggings", Items.LEATHER_LEGGINGS, ArmorType.LEGGINGS,
        ModArmorMaterials.PRISMARINE, ModAttributes.COOLING, 2.0,
        PolymerArmorItem::new);

    public static final PolymerArmorItem PRISMARINE_BOOTS = registerArmor(
        "prismarine_boots", Items.LEATHER_BOOTS, ArmorType.BOOTS,
        ModArmorMaterials.PRISMARINE, ModAttributes.COOLING, 1.0,
        PolymerArmorItem::new);

    // drinkables
    
    public static final Item APPLE_JUICE = register("apple_juice",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0xd18800, 6, 0.4f, 2, 0.3f));

    public static final Item CARROT_JUICE = register("carrot_juice",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0xfa6e02, 6, 0.5f, 2, 0.4f));

    public static final Item CHORUS_FRUIT_SMOOTHIE = register("chorus_fruit_smoothie",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0xc663ff, 5, 0.4f, 3, 0.3f));

    public static final Item SWEET_BERRY_JUICE = register("sweet_berry_juice",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0xdb043e, 6, 0.3f, 2, 0.2f));

    public static final Item GLOW_BERRY_JUICE = register("glow_berry_juice",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0xfac85c, 6, 0.4f, 2, 0.2f));

    public static final Item HOT_COCOA = register("hot_cocoa",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0x382514, 4, 0.6f, 3, 0.6f));

    public static final Item BONE_BROTH = register("bone_broth",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0x736035, 8, 0.7f, 5, 0.7f));

    public static final Item PURIFIED_WATER_BOTTLE = register("purified_water_bottle",
        key -> new ModDrinkItem(new Item.Properties().setId(key), Items.POTION, 0x99c5ff, 8, 0.7f, 0, 0.0f));


    public static final Item COLD_RESISTANCE_POTION = register("cold_resistance_potion",
        key -> new ModPotionItem(new Item.Properties().setId(key),
            ModPotions.COLD_RESIST_POTION_HOLDER, 0x91E7FF));

    public static final Item HEAT_RESISTANCE_POTION = register("heat_resistance_potion",
        key -> new ModPotionItem(new Item.Properties().setId(key),
            ModPotions.HEAT_RESIST_POTION_HOLDER, 0xFF9166));

    public static final Item WATER_BUCKET_DRINKABLE = register("water_bucket_drink",
        key -> new DrinkableWaterBucket(new Item.Properties().setId(key)));




    private static <T extends Item> T register(String name, Function<ResourceKey<Item>, T> factory) {
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(key));
    }


    private static <T extends Item> T registerArmor(
        String name,
        Item clientItem,
        ArmorType armorType,
        ArmorMaterial material,
        Holder<Attribute> bonusAttribute,
        double bonusAmount,
        BiFunction<Item.Properties, Item, T> itemFactory
    ) {
        ResourceKey<Item> itemKey = ResourceKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, name)
        );

        int defenseValue = material.defense().get(armorType);
        EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(armorType.getSlot());

        Item.Properties properties = new Item.Properties()
            .setId(itemKey)
            .durability(defenseValue * 15)

            .component(DataComponents.EQUIPPABLE, Equippable.builder(armorType.getSlot())
                .setAsset(material.assetId())
                .setEquipSound(material.equipSound())
                .build()
            )

            .attributes(ItemAttributeModifiers.builder()
                // base armor defense points
                .add(
                    Attributes.ARMOR,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, "armor_" + name),
                        defenseValue,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    slotGroup
                )
                // custom attribute buff passed into the method
                .add(
                    bonusAttribute,
                    new AttributeModifier(
                        Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, name + "_bonus"),
                        bonusAmount,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    slotGroup
                )
                .build()
            );

        T item = itemFactory.apply(properties, clientItem);
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }
}
