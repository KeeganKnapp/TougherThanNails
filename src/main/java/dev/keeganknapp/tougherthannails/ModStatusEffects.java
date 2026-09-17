package dev.keeganknapp.tougherthannails;

import dev.keeganknapp.tougherthannails.statuseffects.ColdResistanceStatusEffect;
import dev.keeganknapp.tougherthannails.statuseffects.DehydrationStatusEffect;
import dev.keeganknapp.tougherthannails.statuseffects.HeatResistanceStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class ModStatusEffects {

    public static Holder<MobEffect> COLD_RESISTANCE_HOLDER;
    public static Holder<MobEffect> HEAT_RESISTANCE_HOLDER;
    public static Holder<MobEffect> DEHYDRATION_HOLDER;

    public static void register() {
        COLD_RESISTANCE_HOLDER = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath("tougherthannails", "cold_resistance"), new ColdResistanceStatusEffect());

        HEAT_RESISTANCE_HOLDER = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath("tougherthannails", "heat_resistance"), new HeatResistanceStatusEffect());

        DEHYDRATION_HOLDER = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath("tougherthannails", "dehydration"), new DehydrationStatusEffect());


    }



}
