package org.chubby.github.cultcraft.content.ritual;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.entity.player.Player;
import org.chubby.github.cultcraft.content.ritual.tracker.ActiveRitualManager;

public class RitualEventHandler
{
    public static void register()
    {
        TickEvent.SERVER_POST.register(server -> {
            ActiveRitualManager.tickRituals();
        });

        TickEvent.PLAYER_POST.register(player -> {
            ActiveRitualManager.checkAndInterruptIfMovedTooFar(player,5.0D);
        });

        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) ->
        {
            if (livingEntity instanceof Player player)
            {
                if (ActiveRitualManager.isPerformingRitual(player))
                {
                    ActiveRitualManager.interruptRitual(player);
                }
            }
            return EventResult.pass();
        });

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (ActiveRitualManager.isPerformingRitual(player))
            {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });

        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            if (ActiveRitualManager.isPerformingRitual(player))
            {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });

        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            if (ActiveRitualManager.isPerformingRitual(player))
            {
                return CompoundEventResult.interruptFalse(player.getMainHandItem());
            }
            return CompoundEventResult.pass();
        });
    }
}
