package dev.keeganknapp.tougherthannails.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;




public class TemperatureComponent extends FloatComponent {

    public TemperatureComponent() {
        super(12, "Temperature", 0, 25);
    }
}
