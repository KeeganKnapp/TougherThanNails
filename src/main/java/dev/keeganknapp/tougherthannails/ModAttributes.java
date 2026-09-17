package dev.keeganknapp.tougherthannails;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;


public class ModAttributes {
    public static void initialize() {
        // COLD_RESISTANCE / HEAT_RESISTANCE are registered above (static init).
        // Attach them to the player's default attribute supplier so equipment
        // modifiers targeting them actually resolve.
        FabricDefaultAttributeRegistry.MODIFY.register(context ->
            context.modify(EntityType.PLAYER, (type, builder) -> builder
                .add(COLD_RESISTANCE)
                .add(HEAT_RESISTANCE)
                .add(COOLING)
                .add(HEATING)
            )
        );
    }

    public static final Holder<Attribute> COLD_RESISTANCE = register(
        "cold_resistance",
        0.0,
        0.0,
        12.0,
        false
    );

    public static final Holder<Attribute> HEAT_RESISTANCE = register(
        "heat_resistance",
        0.0,
        0.0,
        12.0,
        false
    );

    public static final Holder<Attribute> COOLING = register(
        "cooling",
        0.0,
        0.0,
        12.0,
        false
    );

    public static final Holder<Attribute> HEATING = register(
        "heating",
        0.0,
        0.0,
        12.0,
        false
    );

    private static Holder<Attribute> register(
        String name, double defaultValue, double minValue, double maxValue, boolean syncedWithClient
    ) {
        Identifier identifier = Identifier.fromNamespaceAndPath(TougherThanNails.MOD_ID, name);
        Attribute entityAttribute = new RangedAttribute(
            identifier.toLanguageKey(),
            defaultValue,
            minValue,
            maxValue
        ).setSyncable(syncedWithClient);

        Holder<Attribute> holder = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, identifier, entityAttribute);

        if (!syncedWithClient) {
            PolymerEntityUtils.registerAttribute(holder);
        }

        return holder;
    }





}
