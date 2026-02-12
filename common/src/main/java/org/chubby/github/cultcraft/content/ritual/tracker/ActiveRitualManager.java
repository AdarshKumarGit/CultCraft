package org.chubby.github.cultcraft.content.ritual.tracker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.chubby.github.cultcraft.content.ritual.AbstractRitual;
import org.chubby.github.cultcraft.content.ritual.targeting.RitualTarget;
import org.chubby.github.cultcraft.content.ritual.tracker.RitualState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ActiveRitualManager
{
    public static final Map<UUID, RitualState> activeRituals = new HashMap<>();

    public static void startRitual(Player player, AbstractRitual ritual, BlockPos pos, int duration, RitualTarget target)
    {
        RitualState state = new RitualState(ritual, pos, duration,target);
        activeRituals.put(player.getUUID(), state);
    }

    public static void tickRituals()
    {
        activeRituals.values().forEach(RitualState::tick);
        activeRituals.entrySet().removeIf(entry -> entry.getValue().isComplete());
    }

    public static boolean isPerformingRitual(Player player)
    {
        RitualState state = activeRituals.get(player.getUUID());
        return state != null && state.isActive();
    }

    public static Optional<RitualState> getRitualState(Player player)
    {
        return Optional.ofNullable(activeRituals.get(player.getUUID()));
    }

    public static void interruptRitual(Player player)
    {
        RitualState state = activeRituals.get(player.getUUID());
        if (state != null)
        {
            state.interrupt();
        }
    }

    public static void completeRitual(Player player)
    {
        activeRituals.remove(player.getUUID());
    }

    public static boolean canMove(Player player)
    {
        RitualState state = activeRituals.get(player.getUUID());
        if (state == null) return true;

        return state.getPhase() == RitualState.RitualPhase.PREPARATION;
    }

    public static boolean isNearRitualPosition(Player player, double maxDistance)
    {
        RitualState state = activeRituals.get(player.getUUID());
        if (state == null) return true;

        BlockPos ritualPos = state.getRitualPos();
        return player.position().distanceTo(
                new Vec3(ritualPos.getX(), ritualPos.getY(), ritualPos.getZ())
        ) <= maxDistance;
    }

    public static void checkAndInterruptIfMovedTooFar(Player player, double maxDistance)
    {
        if (isPerformingRitual(player) && !isNearRitualPosition(player, maxDistance))
        {
            interruptRitual(player);
        }
    }
}
