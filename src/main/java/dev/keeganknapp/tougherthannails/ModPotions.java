package dev.keeganknapp.tougherthannails;

import eu.pb4.polymer.core.api.other.PolymerPotion;
import eu.pb4.polymer.core.api.other.SimplePolymerPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {
    public static Holder<Potion> COLD_RESIST_POTION_HOLDER;
    public static Holder<Potion> HEAT_RESIST_POTION_HOLDER;

    public static void register() {
        Potion coldResistPotion = new SimplePolymerPotion(
            "cold_resistance", 
            new MobEffectInstance(
                ModStatusEffects.COLD_RESISTANCE_HOLDER,
                4800,
                0
            )
        );


        COLD_RESIST_POTION_HOLDER = Registry.registerForHolder(
            BuiltInRegistries.POTION,
            Identifier.fromNamespaceAndPath("tougherthannails", "cold_resistance"),
            coldResistPotion
        );

        Potion heatResistPotion = new SimplePolymerPotion(
            "heat_resistance", 
            new MobEffectInstance(
                ModStatusEffects.HEAT_RESISTANCE_HOLDER,
                4800,
                0
            )
        );


        HEAT_RESIST_POTION_HOLDER = Registry.registerForHolder(
            BuiltInRegistries.POTION,
            Identifier.fromNamespaceAndPath("tougherthannails", "heat_resistance"),
            heatResistPotion
        );



    }

 

}
