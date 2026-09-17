package dev.keeganknapp.tougherthannails.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.keeganknapp.tougherthannails.ThirstManager;

public class ModDrinkItem extends Item implements PolymerItem {
    private final Item clientItem;

    int customColor = 0x010101;
    int thirst;
    float thirstSaturation;
    int food;
    float foodSaturation;

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return this.clientItem;
    }

    public ModDrinkItem(Properties properties, Item clientItem, int customColor, int thirst, float thirstSaturation, int food, float foodSaturation) {
        properties
            .stacksTo(16)
            .component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build())
            .usingConvertsTo(Items.GLASS_BOTTLE);
        super(properties);
        this.clientItem = clientItem;
        this.customColor = customColor;
        this.thirst = thirst;
        this.thirstSaturation = thirstSaturation;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return Identifier.fromNamespaceAndPath("minecraft", "potion");
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        // Tells the client this item is consumed via drinking (triggers gulp audio/animations)
        return ItemUseAnimation.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide() && user instanceof Player player) {
            ThirstManager.hydrate(player, this.thirst, this.thirstSaturation);
            player.getFoodData().eat(this.food, this.foodSaturation);
        }
        return super.finishUsingItem(stack, level, user);
    }


    @Override
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipFlag type,
        PacketContext context, HolderLookup.Provider lookup) {
        ItemStack proxy = PolymerItem.super.getPolymerItemStack(stack, type,
            context, lookup);

        PotionContents potionContents = new PotionContents(
            java.util.Optional.empty(),
            java.util.Optional.of(this.customColor),
            java.util.List.of(),
            java.util.Optional.empty()
        );

        proxy.set(DataComponents.POTION_CONTENTS, potionContents);

        // The client sees a minecraft:potion. PotionItem.getName() derives the name
        // purely from POTION_CONTENTS (which we set for the tint), so it ignores
        // ITEM_NAME and would show "Uncraftable Potion". Only CUSTOM_NAME is checked
        // ahead of PotionItem.getName(), so use that, with italics disabled so it
        // doesn't read as an anvil rename.
        proxy.set(DataComponents.CUSTOM_NAME,
            this.getName(stack).copy().withStyle(style -> style.withItalic(false)));
        // Suppress the leftover "No effects" potion tooltip line.
        proxy.set(DataComponents.TOOLTIP_DISPLAY,
            TooltipDisplay.DEFAULT.withHidden(DataComponents.POTION_CONTENTS, true));

        return proxy;
    }
    
}

