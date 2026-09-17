package dev.keeganknapp.tougherthannails.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

public class ModPotionItem extends Item implements PolymerItem {
    private final Holder<Potion> potion;
    private final int color;

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    public ModPotionItem(Properties properties, Holder<Potion> potion, int color) {
        properties
            .component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build())
            .usingConvertsTo(Items.GLASS_BOTTLE)
            .stacksTo(1);
        super(properties);
        this.potion = potion;
        this.color = color;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return Identifier.fromNamespaceAndPath("minecraft", "potion");
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide() && user instanceof Player player) {
            for(MobEffectInstance effect : this.potion.value().getEffects()) {
                player.addEffect(new MobEffectInstance(effect));
            }
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
            java.util.Optional.of(this.color),
            java.util.List.of(),
            java.util.Optional.empty()
        );

        proxy.set(DataComponents.POTION_CONTENTS, potionContents);



        proxy.set(DataComponents.CUSTOM_NAME,
            this.getName(stack).copy().withStyle(style -> style.withItalic(false)));
        proxy.set(DataComponents.TOOLTIP_DISPLAY,
            TooltipDisplay.DEFAULT.withHidden(DataComponents.POTION_CONTENTS, true));
        proxy.set(DataComponents.LORE,
            new ItemLore(fakeEffectLines(potion.value())));

        return proxy;
    }


    private static List<Component> fakeEffectLines(Potion potion) {
        List<Component> lines = new ArrayList<>();
        for (MobEffectInstance inst : potion.getEffects()) {
            MobEffect effect = inst.getEffect().value();
            MutableComponent line = Component.translatable(inst.getDescriptionId());
            if (inst.getAmplifier() > 0) {
                line = Component.translatable("potion.withAmplifier", line,
                    Component.translatable("potion.potency." + inst.getAmplifier()));
            }
            if (!inst.isInfiniteDuration() && inst.getDuration() > 20) {
                int s = inst.getDuration() / 20;
                line = Component.translatable("potion.withDuration", line,
                    Component.literal(String.format("%d:%02d", s / 60, s % 60)));
            }
            lines.add(line.withStyle(style -> style
                .withColor(effect.getCategory().getTooltipFormatting())
                .withItalic(false)
            ));
        }
        return lines;
    }

    
}

