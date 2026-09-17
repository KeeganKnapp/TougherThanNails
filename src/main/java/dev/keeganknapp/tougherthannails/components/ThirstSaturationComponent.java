package dev.keeganknapp.tougherthannails.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


public class ThirstSaturationComponent extends FloatComponent {
    public ThirstSaturationComponent() {
        super(20F, "ThirstSaturation", 0F, 20F);
    }
}
