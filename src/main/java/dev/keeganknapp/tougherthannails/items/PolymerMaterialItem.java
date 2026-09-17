package dev.keeganknapp.tougherthannails.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * A plain (non-equippable) Polymer item that shows up on vanilla clients as {@code clientItem}
 * but with this item's own {@code item_model} so the generated resource pack can give it a
 * custom inventory sprite.
 */
public class PolymerMaterialItem extends Item implements PolymerItem {
    private final Item clientItem;

    public PolymerMaterialItem(Item.Properties properties, Item clientItem) {
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
        ItemStack proxy = PolymerItem.super.getPolymerItemStack(stack, type, context, lookup);
        proxy.set(DataComponents.ITEM_MODEL, BuiltInRegistries.ITEM.getKey(this));
        return proxy;
    }
}
