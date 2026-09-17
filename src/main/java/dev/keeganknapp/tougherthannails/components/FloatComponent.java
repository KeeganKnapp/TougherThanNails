package dev.keeganknapp.tougherthannails.components;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class FloatComponent implements AutoSyncedComponent {
    protected final float DEFAULT_VALUE;
    public final String NAME;
    public final float MAX;
    public final float MIN;


    protected float value;


    public FloatComponent(float value, String name, float min, float max) {
        this.value = value;
        this.NAME = name;
        this.MAX = max;
        this.MIN = min;
        this.DEFAULT_VALUE = value;
    }




    public float getValue() { return this.value; }

    public void setValue(float amt) { this.value = clamp(amt); }
    public void increment(float amt) { this.value=  clamp(value + amt); }
    public void decrement(float amt) { this.value=  clamp(value - amt); }

    public float incrementAndReturnRestored(float amountPassedIn) { 
        float spaceLeft = this.MAX - this.value;

        float actualPointsRestored = Math.min(amountPassedIn, spaceLeft);

        this.value += actualPointsRestored;

        return actualPointsRestored;
    }

    public float clamp(float val) {
        return Math.clamp(val, this.MIN, this.MAX);
    }

    public void readData(ValueInput readView) {
        this.value = readView.getFloatOr(NAME, 12);
    }

    public void writeData(ValueOutput writeView) {
        writeView.putFloat("Temperature", this.value);
    }

    public void readFromNbt(CompoundTag tag) {
        if (tag.contains(this.NAME)) {
            this.value = tag.getFloatOr(this.NAME, 12);
        } else {
            this.value = this.DEFAULT_VALUE; // Default fallback for a brand new player
        }
    }

    public void writeToNbt(CompoundTag tag) {
        // Permanently saves the player's value level to their .dat file on disk
        tag.putFloat(this.NAME, this.value);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return false;
    }

    

}
