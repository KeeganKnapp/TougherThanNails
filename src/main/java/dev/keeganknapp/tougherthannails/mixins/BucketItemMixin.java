package dev.keeganknapp.tougherthannails.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import dev.keeganknapp.tougherthannails.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @ModifyReturnValue(method = "use", at = @At("RETURN"))
    private InteractionResult ttn$drinkIfNotPlacing(InteractionResult original,
        Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.WATER_BUCKET) || player.isSpectator()) return original;

        // matches "not aiming at a block". Use `instanceof InteractionResult.Success`
        // instead if you also want to drink when placement was blocked.
        if (!(original instanceof InteractionResult.Pass)) return original;

        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable == null) return original;              // ItemsMixin supplies it


        player.setItemInHand(hand, new ItemStack(ModItems.WATER_BUCKET_DRINKABLE, 1));
        return consumable.startConsuming(player, player.getItemInHand(hand), hand); // begins the 1.6s drink
    }
}

