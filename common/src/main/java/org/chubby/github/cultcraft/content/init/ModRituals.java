package org.chubby.github.cultcraft.content.init;

import org.chubby.github.cultcraft.CultCraft;
import org.chubby.github.cultcraft.content.ritual.RitualRegistry;
import org.chubby.github.cultcraft.content.ritual.compoents.RitualStructure;
import org.chubby.github.cultcraft.content.ritual.rituals.HealingRitual;

public class ModRituals
{
    public static final RitualRegistry<HealingRitual> HEALING_RITUAL = new RitualRegistry<>(CultCraft.loc("healing_ritual"),
            () -> new HealingRitual(new RitualStructure.Builder().tier(RitualStructure.StructureTier.TIER_I).build()));

    public static void init(){}
}
