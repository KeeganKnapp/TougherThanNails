package dev.keeganknapp.tougherthannails.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


public class BodyHeatComponent extends FloatComponent {
    public BodyHeatComponent() {
        super(0F, "BodyHeat", 0F, 3F);
    }
}
