package org.chubby.github.cultcraft.content.ritual.targeting;


public enum RitualTargetMode
{
    SELF,
    TARGET_ENTITY,
    TARGET_AREA,
    TARGET_LOCATION,
    TARGET_BOUND,
    TARGET_EFFIGY,
    TARGET_BLOOD_LINK,
    GLOBAL;

    public boolean requiresTarget()
    {
        return this != SELF && this != GLOBAL && this != TARGET_AREA;
    }

    public boolean canTargetPlayers()
    {
        return this == TARGET_ENTITY || this == TARGET_BOUND || this == TARGET_EFFIGY || this == TARGET_BLOOD_LINK;
    }

    public boolean canTargetMobs()
    {
        return this == TARGET_ENTITY || this == TARGET_BOUND;
    }

    public boolean hasAreaEffect()
    {
        return this == TARGET_AREA || this == GLOBAL;
    }
}