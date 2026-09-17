package dev.keeganknapp.tougherthannails;

import java.util.List;


import dev.keeganknapp.tougherthannails.components.FloatComponent;
import dev.keeganknapp.tougherthannails.components.ThirstComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand; 
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ThirstManager {
    private static int tickCounter = 0;

    

    public static void init() {
        UseItemCallback.EVENT.register((player, world, hand) -> tryDrink(player, world, hand));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> tryDrink(player, world, hand));
    }

    private static InteractionResult tryDrink(Player player, Level world, InteractionHand hand) {
        if (world.isClientSide()) return InteractionResult.PASS;
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;

        double reach = 4.5;
        var eye = player.getEyePosition(1.0F);
        var look = player.getViewVector(1.0F);
        var end = eye.add(look.scale(reach));

        BlockHitResult hit = world.clip(new ClipContext(
            eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player));

        if (hit.getType() != BlockHitResult.Type.BLOCK) return InteractionResult.PASS;

        // covers water source, flowing water, and waterlogged blocks
        if (!world.getFluidState(hit.getBlockPos()).is(FluidTags.WATER)) return InteractionResult.PASS;

        hydrateWithChanceOfSickness(player,
            TTNTuningConfig.WATER_BLOCK_THIRST, TTNTuningConfig.WATER_BLOCK_SATURATION);
        return InteractionResult.SUCCESS; // swings arm, suppresses vanilla block use
    }

 

    public static void onServerTick(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
    
        //fire once every second
        tickCounter++;
        if (tickCounter % 20 != 0) return; 

        for(ServerPlayer player : players) {

            if(player.gameMode().isCreative()) continue;
            reduceThirstDeltaFromTemperature(player);
        }

    }

    public static int getPlayerThirst(Player player) {
        return (int)Math.round(ModComponents.THIRST.get(player).getValue());
    }

    public static int getPlayerThirstSaturation(Player player) {
        return (int)Math.round(ModComponents.THIRST_SATURATION.get(player).getValue());
    }



    public static void reduceThirst(Player player, float amt) {
        FloatComponent sat = ModComponents.THIRST_SATURATION.get(player);
        float fromSat = Math.min(sat.getValue(), amt);
        sat.increment(-fromSat);

        if (amt > fromSat) ModComponents.THIRST.get(player).increment(-(amt - fromSat));
    }


    public static void hydrate(Player player, float thirst, float saturationModifier) {
        ThirstComponent playerThirst = (ThirstComponent) ModComponents.THIRST.get(player);
        playerThirst.increment(thirst);
        
        float totalSaturation = thirst * saturationModifier;

        FloatComponent playerThirstSaturation = ModComponents.THIRST_SATURATION.get(player);
        playerThirstSaturation.increment(totalSaturation);

        return; 
    }


    public static void hydrateWithChanceOfSickness(Player player, float thirst, float saturation) {
        // Need to implement sickness
        RandomSource random = RandomSource.create();

        if(random.nextFloat() < TTNTuningConfig.CHANCE_OF_DEHYDRATION_FROM_DIRTY_WATER)
            player.addEffect(new MobEffectInstance(ModStatusEffects.DEHYDRATION_HOLDER, 600, 0, false, false, true));

        hydrate(player, thirst, saturation);
    }

    public static void reduceThirstDeltaFromTemperature(Player player) {
        float ambient = Math.clamp(TemperatureManager.getEnvironmentalTemp((ServerPlayer) player), 0f, 25f);
        float thirstDelta = (ambient + 5.0f) / 225.0f;   // same curve, ambient-only input
        
        if(player.hasEffect(ModStatusEffects.DEHYDRATION_HOLDER)) {
            thirstDelta *= TTNTuningConfig.DEHYDRATION_THIRST_DELTA_MULTIPLIER;
        }


        reduceThirst(player, thirstDelta);
    }

 

    public static void drinkItem(Player player, ItemStack stack) {
        if(stack.is(Items.POTION)) {
            // real potions don't give sickness
            if(stack.get(DataComponents.POTION_CONTENTS).hasEffects())
                hydrate(player, TTNTuningConfig.POTION_THIRST, TTNTuningConfig.POTION_SATURATION); 

            // potions with no effects give sickness (empty water bottle)
            else
                hydrateWithChanceOfSickness(player, TTNTuningConfig.POTION_THIRST, TTNTuningConfig.POTION_SATURATION); 
        }
        if(stack.is(ModItems.WATER_BUCKET_DRINKABLE)) {
            hydrate(player, TTNTuningConfig.WATER_BUCKET_THIRST, TTNTuningConfig.WATER_BUCKET_SATURATION); 
        }
        if(stack.is(Items.MILK_BUCKET)) {
            hydrate(player, TTNTuningConfig.MILK_BUCKET_THIRST, TTNTuningConfig.MILK_BUCKET_SATURATION); 
        }
        if(stack.is(ModItems.HEAT_RESISTANCE_POTION)) {
            hydrate(player, TTNTuningConfig.POTION_THIRST, TTNTuningConfig.POTION_SATURATION); 
        }
        if(stack.is(ModItems.COLD_RESISTANCE_POTION)) {
            hydrate(player, TTNTuningConfig.POTION_THIRST, TTNTuningConfig.POTION_SATURATION); 
        }

    }


}
