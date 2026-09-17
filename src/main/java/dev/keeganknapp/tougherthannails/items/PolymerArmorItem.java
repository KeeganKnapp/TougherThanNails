package dev.keeganknapp.tougherthannails.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class PolymerArmorItem extends Item implements PolymerItem {
    private final Item clientItem;

    public PolymerArmorItem(Item.Properties properties, Item clientItem) {
        super(properties);
        this.clientItem = clientItem;
    }

    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        return this.clientItem;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipFlag type,
        PacketContext context, HolderLookup.Provider lookup) {
        ItemStack proxy = PolymerItem.super.getPolymerItemStack(stack, type,
            context, lookup);
        proxy.set(DataComponents.EQUIPPABLE,
            stack.get(DataComponents.EQUIPPABLE));
        // carry the attribute modifiers over so the client renders the tooltip lines
        proxy.set(DataComponents.ATTRIBUTE_MODIFIERS,
            stack.get(DataComponents.ATTRIBUTE_MODIFIERS));
        // give the inventory/held sprite its own look too
        proxy.set(DataComponents.ITEM_MODEL, BuiltInRegistries.ITEM.getKey(this));
        return proxy;
    }
}

