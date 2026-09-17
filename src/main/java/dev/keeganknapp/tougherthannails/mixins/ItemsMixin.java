package dev.keeganknapp.tougherthannails.mixins;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Function;

@Mixin(Items.class)
public class ItemsMixin {

    @Inject(
        method = "registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
        at = @At("HEAD")
    )
    private static void injectWaterBucketComponents(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties, CallbackInfoReturnable<Item> cir) {
        // Target the vanilla registry key name for the water bucket
        if ("water_bucket".equals(name)) {
            
            FoodProperties food = new FoodProperties.Builder()
                    .nutrition(0)
                    .saturationModifier(0.0f)
                    .alwaysEdible() 
                    .build();

            Consumable consumable = Consumable.builder()
                    .consumeSeconds(1.6f) 
                    .animation(ItemUseAnimation.DRINK)
                    .sound(SoundEvents.GENERIC_DRINK)
                    .hasConsumeParticles(false)
                    .build();

            properties.component(DataComponents.FOOD, food);
            properties.component(DataComponents.CONSUMABLE, consumable);
        }
    }
}
