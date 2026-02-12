package org.chubby.github.cultcraft.content.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.chubby.github.cultcraft.api.capability.ICapability;
import org.chubby.github.cultcraft.api.capability.PlayerCapability;
import org.chubby.github.cultcraft.content.ritual.AbstractRitual;

import java.util.ArrayList;
import java.util.List;

public class KnownRitualsCap extends PlayerCapability
{
    public static final String ID = "cultcraft:known_rituals";
    public final List<AbstractRitual> knownRituals = new ArrayList<>();


    @Override
    public String getCapabilityId() {
        return ID;
    }

    public List<AbstractRitual> getKnownRituals() {
        return knownRituals;
    }

    public void addRitual(AbstractRitual ritual)
    {
        knownRituals.add(ritual);
    }

    public void removeRitual(AbstractRitual ritual)
    {
        knownRituals.remove(ritual);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for(AbstractRitual ritual : knownRituals)
        {
            //listTag.add(ritual.serialize(tag));
        }
        tag.put("knownRituals",listTag);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {

    }

    @Override
    public void copyFrom(ICapability other) {

    }
}
