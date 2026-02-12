package org.chubby.github.cultcraft.content.capability;

import net.minecraft.nbt.CompoundTag;
import org.chubby.github.cultcraft.api.capability.ICapability;
import org.chubby.github.cultcraft.api.capability.PlayerCapability;

public class PlayerAlignmentCap extends PlayerCapability
{
    public static final String ID = "cultcraft:player_alignment";
    public static final int MAX_ALIGNMENT = 1000;
    public static final int MIN_ALIGNMENT = -(1000);

    public int alignmentLevel = 0;
    @Override
    public String getCapabilityId() {
        return ID;
    }

    public int getAlignmentLevel() {
        return alignmentLevel;
    }

    public void increaseAlignment(int level)
    {
        this.alignmentLevel += level;
        markDirty();
    }

    public void decreaseAlignment(int level)
    {
        this.alignmentLevel -= level;
        markDirty();
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("alignmentLevel",alignmentLevel);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        this.alignmentLevel = tag.getInt("alignmentLevel");
    }

    @Override
    public void copyFrom(ICapability other) {
        if(other instanceof PlayerAlignmentCap cap)
        {
            this.alignmentLevel = cap.alignmentLevel;
            markDirty();
        }
    }
}
