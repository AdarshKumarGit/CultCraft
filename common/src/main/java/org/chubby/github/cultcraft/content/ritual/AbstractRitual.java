package org.chubby.github.cultcraft.content.ritual;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.chubby.github.cultcraft.content.ritual.compoents.*;
import org.chubby.github.cultcraft.content.ritual.targeting.*;
import org.chubby.github.cultcraft.content.ritual.tracker.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractRitual
{
    private final BloodType bloodType;
    private final int bloodCost;
    private final List<ICatalyst> catalysts;
    private final RitualStructure requiredStructure;
    private final List<EnvironmentalCondition> environmentalConditions;
    private final List<IRitualFailEffect> failEffects;
    private final RitualCategory ritualCategory;
    private final int duration;
    private final RitualTargetMode targetMode;
    private final ITargetingMethod targetingMethod;

    protected AbstractRitual(
            BloodType bloodType,
            int bloodCost,
            List<ICatalyst> catalysts,
            RitualStructure requiredStructure,
            List<EnvironmentalCondition> environmentalConditions,
            List<IRitualFailEffect> failEffects,
            RitualCategory ritualCategory,
            int duration,
            RitualTargetMode targetMode,
            ITargetingMethod targetingMethod)
    {
        this.bloodType = bloodType;
        this.bloodCost = bloodCost;
        this.catalysts = catalysts;
        this.requiredStructure = requiredStructure;
        this.environmentalConditions = environmentalConditions;
        this.failEffects = failEffects;
        this.ritualCategory = ritualCategory;
        this.duration = duration;
        this.targetMode = targetMode;
        this.targetingMethod = targetingMethod;
    }

    public RitualResult execute(Level level, BlockPos pos, Player performer)
    {
        if (ActiveRitualManager.isPerformingRitual(performer))
        {
            return RitualResult.failure("Already performing a ritual");
        }

        if (!canPerform(level, pos, performer))
        {
            return RitualResult.failure("Requirements not met");
        }

        if (!checkEnvironmentalConditions(level, pos))
        {
            applyFailEffects(level, pos, performer);
            return RitualResult.failure("Environmental conditions not met");
        }

        if (!requiredStructure.isValid(level, pos))
        {
            applyFailEffects(level, pos, performer);
            return RitualResult.failure("Invalid ritual structure");
        }

        Optional<RitualTarget> target = acquireTarget(level, pos, performer);
        if (targetMode.requiresTarget() && target.isEmpty())
        {
            applyFailEffects(level, pos, performer);
            return RitualResult.failure("Failed to acquire target");
        }

        if (target.isPresent() && !target.get().isValid(level))
        {
            applyFailEffects(level, pos, performer);
            return RitualResult.failure("Target is no longer valid");
        }

        if (!consumeResources(level, pos, performer))
        {
            applyFailEffects(level, pos, performer);
            return RitualResult.failure("Insufficient resources");
        }

        ActiveRitualManager.startRitual(performer, this, pos, duration, target.orElse(null));

        return RitualResult.success();
    }

    protected Optional<RitualTarget> acquireTarget(Level level, BlockPos pos, Player performer)
    {
        if (targetingMethod != null)
        {
            return targetingMethod.acquireTarget(level, pos, performer);
        }

        if (targetMode == RitualTargetMode.SELF)
        {
            return Optional.of(RitualTarget.self());
        }

        return Optional.empty();
    }

    public void completeRitual(Level level, BlockPos pos, Player performer, RitualTarget target)
    {
        boolean success = performRitual(level, pos, performer, target);

        if (!success)
        {
            applyFailEffects(level, pos, performer);
        }
        else
        {
            onRitualSuccess(level, pos, performer, target);
        }

        ActiveRitualManager.completeRitual(performer);
    }

    protected boolean checkEnvironmentalConditions(Level level, BlockPos pos)
    {
        for (EnvironmentalCondition condition : environmentalConditions)
        {
            if (!condition.isMet(level, pos))
            {
                return false;
            }
        }
        return true;
    }

    protected void applyFailEffects(Level level, BlockPos pos, Player performer)
    {
        for (IRitualFailEffect effect : failEffects)
        {
            effect.apply(level, pos, performer);
        }
    }

    protected abstract boolean performRitual(Level level, BlockPos pos, Player performer, RitualTarget target);

    protected abstract boolean canPerform(Level level, BlockPos pos, Player performer);

    protected abstract boolean consumeResources(Level level, BlockPos pos, Player performer);

    protected void onRitualSuccess(Level level, BlockPos pos, Player performer, RitualTarget target)
    {

    }

    public BloodType getBloodType()
    {
        return bloodType;
    }

    public int getBloodCost()
    {
        return bloodCost;
    }

    public List<ICatalyst> getCatalysts()
    {
        return new ArrayList<>(catalysts);
    }

    public RitualStructure getRequiredStructure()
    {
        return requiredStructure;
    }

    public List<EnvironmentalCondition> getEnvironmentalConditions()
    {
        return new ArrayList<>(environmentalConditions);
    }

    public List<IRitualFailEffect> getFailEffects()
    {
        return new ArrayList<>(failEffects);
    }

    public RitualCategory getRitualCategory()
    {
        return ritualCategory;
    }

    public int getDuration()
    {
        return duration;
    }

    public RitualTargetMode getTargetMode()
    {
        return targetMode;
    }

    public ITargetingMethod getTargetingMethod()
    {
        return targetingMethod;
    }

    public static class RitualResult
    {
        private final boolean successful;
        private final String message;

        private RitualResult(boolean successful, String message)
        {
            this.successful = successful;
            this.message = message;
        }

        public static RitualResult success()
        {
            return new RitualResult(true, "Ritual started successfully");
        }

        public static RitualResult failure(String reason)
        {
            return new RitualResult(false, reason);
        }

        public boolean isSuccessful()
        {
            return successful;
        }

        public String getMessage()
        {
            return message;
        }
    }
}