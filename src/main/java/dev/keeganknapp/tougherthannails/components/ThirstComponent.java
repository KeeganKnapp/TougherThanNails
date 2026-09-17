package dev.keeganknapp.tougherthannails.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


public class ThirstComponent extends FloatComponent {
    public ThirstComponent() {
        super(20F, "Thirst", 0F, 20F);
    }
}
