package org.chubby.github.cultcraft.util;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.chubby.github.cultcraft.content.ritual.AbstractRitual;
import org.jetbrains.annotations.Nullable;

public class ModEventHandler
{
    public static void register()
    {
        PlayerEvent.ATTACK_ENTITY.register(ModEventHandler::onAttackEntity);
        TickEvent.Player.PLAYER_POST.register(ModEventHandler::onPlayerTick);
    }

    private static void onPlayerTick(Player player)
    {

    }

    private static EventResult onAttackEntity(Player player, Level level, Entity entity, InteractionHand interactionHand, @Nullable EntityHitResult hitResult)
    {
        return null;
    }
}
