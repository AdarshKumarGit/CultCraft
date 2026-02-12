package org.chubby.github.cultcraft.content.ritual.tracker;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import org.chubby.github.cultcraft.content.ritual.AbstractRitual;
import org.chubby.github.cultcraft.content.ritual.targeting.RitualTarget;
import java.util.Optional;

public class RitualState
{
    private final AbstractRitual ritual;
    private final BlockPos ritualPos;
    private final RitualTarget target;
    private final long startTime;
    private final int totalDuration;
    private int ticksRemaining;
    private RitualPhase phase;

    public RitualState(AbstractRitual ritual, BlockPos pos, int duration, RitualTarget target)
    {
        this.ritual = ritual;
        this.ritualPos = pos;
        this.target = target;
        this.totalDuration = duration;
        this.ticksRemaining = duration;
        this.startTime = System.currentTimeMillis();
        this.phase = RitualPhase.PREPARATION;
    }

    public void tick()
    {
        if (ticksRemaining > 0)
        {
            ticksRemaining--;
            updatePhase();
        }
    }

    private void updatePhase()
    {
        float progress = 1.0f - ((float) ticksRemaining / totalDuration);

        if (progress < 0.3f)
        {
            phase = RitualPhase.PREPARATION;
        }
        else if (progress < 0.7f)
        {
            phase = RitualPhase.CHANNELING;
        }
        else if (progress < 1.0f)
        {
            phase = RitualPhase.CULMINATION;
        }
        else
        {
            phase = RitualPhase.COMPLETE;
        }
    }

    public boolean isComplete()
    {
        return ticksRemaining <= 0;
    }

    public boolean isActive()
    {
        return ticksRemaining > 0 && phase != RitualPhase.COMPLETE;
    }

    public AbstractRitual getRitual()
    {
        return ritual;
    }

    public BlockPos getRitualPos()
    {
        return ritualPos;
    }

    public Optional<RitualTarget> getTarget()
    {
        return Optional.ofNullable(target);
    }

    public int getTicksRemaining()
    {
        return ticksRemaining;
    }

    public RitualPhase getPhase()
    {
        return phase;
    }

    public float getProgress()
    {
        return 1.0f - ((float) ticksRemaining / totalDuration);
    }

    public void interrupt()
    {
        this.phase = RitualPhase.INTERRUPTED;
        this.ticksRemaining = 0;
    }

    public boolean isInterrupted()
    {
        return phase == RitualPhase.INTERRUPTED;
    }

    public enum RitualPhase
    {
        PREPARATION,
        CHANNELING,
        CULMINATION,
        COMPLETE,
        INTERRUPTED
    }
}