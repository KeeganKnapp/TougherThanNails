package dev.keeganknapp.tougherthannails.items;


import org.jetbrains.annotations.Nullable;

import dev.keeganknapp.tougherthannails.TTNTuningConfig;
import dev.keeganknapp.tougherthannails.ThirstManager;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

public class DrinkableWaterBucket extends Item implements PolymerItem {


    public DrinkableWaterBucket(Properties properties) {
        properties
            .stacksTo(1)
            .component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build())
            .usingConvertsTo(Items.BUCKET)
            .component(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath("minecraft", "water_bucket"));
        super(properties);
    }

    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        return Items.MILK_BUCKET; // full bucket model; texture swapped via custom_model_data
    }

    

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context, HolderLookup.Provider lookup){
        // 1. Let Polymer execute its standard mapping behavior first
        PolymerItem.super.modifyBasePolymerItemStack(out, stack, context, lookup);

        // 2. Overwrite the client stack's model component to target the vanilla water bucket asset
        // 'minecraft:water_bucket' forces the milk bucket layout to render with the water bucket texture/model
        out.set(DataComponents.ITEM_MODEL, Identifier.withDefaultNamespace("water_bucket"));
    }


    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }     

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {                         
        if (!level.isClientSide() && user instanceof Player player) {                                                                                        
                ThirstManager.hydrateWithChanceOfSickness(player,                                 
                TTNTuningConfig.WATER_BUCKET_THIRST, TTNTuningConfig.WATER_BUCKET_SATURATION);
        }

        return super.finishUsingItem(stack, level, user);
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipFlag type, PacketContext context, HolderLookup.Provider lookup) {
        ItemStack proxy = PolymerItem.super.getPolymerItemStack(stack, type, context, lookup);
        proxy.set(DataComponents.CUSTOM_NAME,
            this.getName(stack).copy().withStyle(s -> s.withItalic(false)));
        return proxy;
    }   
}
