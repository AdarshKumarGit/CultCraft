package org.chubby.github.cultcraft.content.ritual.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.chubby.github.cultcraft.content.ritual.AbstractRitual;
import org.chubby.github.cultcraft.content.ritual.compoents.*;
import org.chubby.github.cultcraft.content.ritual.targeting.ITargetingMethod;
import org.chubby.github.cultcraft.content.ritual.targeting.RitualTarget;
import org.chubby.github.cultcraft.content.ritual.targeting.RitualTargetMode;

import java.util.List;

public class HealingRitual extends AbstractRitual
{

    public HealingRitual(RitualStructure structure)
    {
        super(
                BloodType.RAW_BLOOD,
                50,
                List.of(),
                structure,
                List.of(
                        new EnvironmentalCondition.TimeCondition(0, 12000),
                        new EnvironmentalCondition.WeatherCondition(EnvironmentalCondition.WeatherType.CLEAR)
                ),
                List.of(
                        new IRitualFailEffect.DamageEffect(5.0f),
                        new IRitualFailEffect.StatusEffect(MobEffects.WEAKNESS, 200, 0)
                ),
                RitualCategory.HEALING,
                100,
                RitualTargetMode.SELF,
                new ITargetingMethod.SelfTargeting()
        );
    }

    @Override
    protected boolean performRitual(Level level, BlockPos pos, Player performer, RitualTarget target) {
        performer.heal(10.0f);
        return true;
    }

    @Override
    protected boolean canPerform(Level level, BlockPos pos, Player performer)
    {
        return performer.getHealth() < performer.getMaxHealth();
    }

    @Override
    protected boolean consumeResources(Level level, BlockPos pos, Player performer)
    {
        return true;
    }
}
