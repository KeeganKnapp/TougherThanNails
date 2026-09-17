package dev.keeganknapp.tougherthannails.mixins;

import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.keeganknapp.tougherthannails.ModItems;
import dev.keeganknapp.tougherthannails.ThirstManager;

@Mixin(Mob.class)
public class MobMixin {
    private static final List<EquipmentSlot> EQUIPMENT_POPULATION_ORDER = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);


    //@Inject(method = "getEquipmentForSlot", at = @At("HEAD"), cancellable = true)
    private static @Nullable Item getModdedEquipmentForSlot(final EquipmentSlot slot, final int type) {
        switch (slot) {
            case HEAD:
            if (type >= 0 && type <=5) {
                return ModItems.FUR_HIDE_HELMET;
            }
            break;
            case CHEST:
            if (type >= 0 && type <=5) {
                return ModItems.FUR_HIDE_CHESTPLATE;
            }
            break;
            case LEGS:
            if (type >= 0 && type <=5) {
                return ModItems.FUR_HIDE_LEGGINGS;
            }
            break;
            case FEET:
            if (type >= 0 && type <=5) {
                return ModItems.FUR_HIDE_BOOTS;
            }
            break;
            default:
                break;
        }

        return Items.AIR;
    }



    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void populateDefaultEquipmentSlots(final RandomSource random, final DifficultyInstance difficulty, CallbackInfo cir) {
        Mob mob = (Mob) (Object) this;


        float chanceStartArmor = 0.15F;
        boolean coldBiome = false;

        if(mob.level().getBiome(mob.blockPosition()).value().getBaseTemperature() <= 0.25F) {
            coldBiome = true;
            chanceStartArmor = 0.50F;
        }

        if (random.nextFloat() < chanceStartArmor * difficulty.getSpecialMultiplier()) {
            int armorType = random.nextInt(3);

            for (int i = 1; i <= 3.0F; i++) {
                if (random.nextFloat() < 0.1087F) {
                    armorType++;
                }
            }

            float partialChance = mob.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
            boolean first = true;

            for (EquipmentSlot slot : EQUIPMENT_POPULATION_ORDER) {
                ItemStack itemStack = mob.getItemBySlot(slot);
                if (!first && random.nextFloat() < partialChance) {
                    break;
                }

                first = false;
                if (itemStack.isEmpty()) {
                    Item equip;
                    if(coldBiome)
                        equip = getModdedEquipmentForSlot(slot, armorType);
                    else
                        equip = Mob.getEquipmentForSlot(slot, armorType); 

                    if (equip != null) {
                        mob.setItemSlot(slot, new ItemStack(equip));
                    }
                }
            }
        }
    }

}

