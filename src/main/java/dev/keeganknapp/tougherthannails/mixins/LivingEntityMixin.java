package dev.keeganknapp.tougherthannails.mixins;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.keeganknapp.tougherthannails.ModItems;
import dev.keeganknapp.tougherthannails.ThirstManager;
import dev.keeganknapp.tougherthannails.items.DrinkableWaterBucket;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow protected ItemStack useItem;
    @Shadow protected int useItemRemaining;

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void onCompleteUsingItem(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.level().isClientSide() && entity instanceof ServerPlayer player) {
            ItemStack stack = entity.getUseItem();

            if (stack != null && !stack.isEmpty()) {
                if (stack.getUseAnimation() == ItemUseAnimation.DRINK) {
                    ThirstManager.drinkItem(player, stack);
                }

            }
        }
    }

    @Inject(method = "releaseUsingItem", at = @At("HEAD"))
    private void onDrinkingInterrupted(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // Ensure this logic only runs on the server and for a Player
        if (!entity.level().isClientSide() && entity instanceof Player player) {

            // Check if the item they were using is your custom item class
            if (!this.useItem.isEmpty() && this.useItem.getItem() instanceof DrinkableWaterBucket) {
                InteractionHand hand = player.getUsedItemHand();

                player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET, 1));
            }
        }
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void replaceDrinkableWaterBucketsNotInHand(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player) {
            int oldSlot = -1;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.is(ModItems.WATER_BUCKET_DRINKABLE)) {
                    oldSlot = i;
                    break;
                }
            }

            if (oldSlot != -1 && oldSlot != player.getInventory().getSelectedSlot()) {
                player.getInventory().setItem(oldSlot, new ItemStack(Items.WATER_BUCKET, 1));

                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    serverPlayer.containerMenu.broadcastChanges();
                }
            }
        }
    }

}

