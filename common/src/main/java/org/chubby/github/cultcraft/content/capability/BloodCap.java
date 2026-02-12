package org.chubby.github.cultcraft.content.capability;

import net.minecraft.nbt.CompoundTag;
import org.chubby.github.cultcraft.api.capability.ICapability;
import org.chubby.github.cultcraft.api.capability.PlayerCapability;

public class BloodCap extends PlayerCapability
{

    public static final String ID = "cultcraft:blood";
    public static final int MAX_BLOOD_AMOUNT = 100;

    public int bloodAmt = 0;

    public BloodCap()
    {
        this.bloodAmt = MAX_BLOOD_AMOUNT;
    }

    @Override
    public String getCapabilityId() {
        return ID;
    }

    public int getBloodAmt() {
        return bloodAmt;
    }

    public void setBloodAmt(int amt) {
        int clamped = Math.max(0, Math.min(MAX_BLOOD_AMOUNT, amt));
        if (this.bloodAmt != clamped) {
            this.bloodAmt = clamped;
            markDirty();
        }
    }

    public void addBlood(int amt) {
        setBloodAmt(this.bloodAmt + amt);
    }

    public void removeBlood(int amt) {
        setBloodAmt(this.bloodAmt - amt);
    }

    public boolean hasBlood(int amt) {
        return this.bloodAmt >= amt;
    }

    public boolean isFull() {
        return this.bloodAmt >= MAX_BLOOD_AMOUNT;
    }

    public boolean isEmpty() {
        return this.bloodAmt <= 0;
    }

    public float getBloodPercentage() {
        return (float) bloodAmt / MAX_BLOOD_AMOUNT;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("bloodAmount", bloodAmt);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        this.bloodAmt = tag.getInt("bloodAmount");
    }

    @Override
    public void copyFrom(ICapability other) {
        if (other instanceof BloodCap blood) {
            this.bloodAmt = blood.bloodAmt;
            markDirty();
        }
    }
}
