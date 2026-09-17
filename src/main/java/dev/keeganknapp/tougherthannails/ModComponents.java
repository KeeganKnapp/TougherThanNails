package dev.keeganknapp.tougherthannails;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import dev.keeganknapp.tougherthannails.components.BodyHeatComponent;
import dev.keeganknapp.tougherthannails.components.FloatComponent;
import dev.keeganknapp.tougherthannails.components.TemperatureComponent;
import dev.keeganknapp.tougherthannails.components.ThirstComponent;
import dev.keeganknapp.tougherthannails.components.ThirstSaturationComponent;
import net.minecraft.resources.Identifier;

public final class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<FloatComponent> THIRST =
        ComponentRegistry.getOrCreate(Identifier.parse("tougherthannails:thirst"), FloatComponent.class);
    public static final ComponentKey<FloatComponent> TEMPERATURE =
        ComponentRegistry.getOrCreate(Identifier.parse("tougherthannails:temperature"), FloatComponent.class);
    public static final ComponentKey<FloatComponent> THIRST_SATURATION =
        ComponentRegistry.getOrCreate(Identifier.parse("tougherthannails:thirst_saturation"), FloatComponent.class);
    public static final ComponentKey<FloatComponent> BODY_HEAT =
        ComponentRegistry.getOrCreate(Identifier.parse("tougherthannails:body_heat"), FloatComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // registerForPlayers targets Player.class internally, so this already covers
        // ServerPlayer (and any client player subclass) via CCA's isAssignableFrom
        // dispatch - no separate ServerPlayer.class/Player.class registration needed.
        registry.registerForPlayers(THIRST, player -> new ThirstComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(TEMPERATURE, player -> new TemperatureComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(THIRST_SATURATION, player -> new ThirstSaturationComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(BODY_HEAT, player -> new BodyHeatComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
