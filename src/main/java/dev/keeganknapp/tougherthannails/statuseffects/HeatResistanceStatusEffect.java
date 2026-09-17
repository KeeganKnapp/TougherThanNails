package dev.keeganknapp.tougherthannails.statuseffects;

import org.jetbrains.annotations.Nullable;

import dev.keeganknapp.tougherthannails.ModAttributes;
import dev.keeganknapp.tougherthannails.TougherThanNails;
import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;



public class HeatResistanceStatusEffect extends MobEffect implements PolymerMobEffect { 
    
    public HeatResistanceStatusEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xff9a47);

        addAttributeModifier(ModAttributes.HEAT_RESISTANCE, Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, "heat_resistance"), 7,  net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE);
    }


    @Override
    public @Nullable MobEffect getPolymerReplacement(MobEffect potion, PacketContext context) {
        // Returning null guarantees no custom packets are sent to vanilla clients.
        // The client will not see an icon, duration timer, or ambient particles.
        return null; 
    }

}
