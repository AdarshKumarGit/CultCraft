package org.chubby.github.cultcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.chubby.github.cultcraft.blocks.entity.ChaliceBlockEntity;
import org.chubby.github.cultcraft.content.init.ModItems;

public class PlayerHelper
{
    private static final int BLOOD_TRANSFER_AMOUNT = 10;

    /**
     * Transfer blood from player to chalice using a dagger
     * @param player The player
     * @param level The world
     * @param clickedPos The clicked pos
     * @return true if blood was successfully transferred
     */
    public static boolean addBloodToChalice(Player player, Level level, BlockPos clickedPos)
    {
        // Check if player is holding a dagger
        if (!player.getMainHandItem().is(ModItems.DAGGER.get()))
        {
            return false;
        }

        // Check if the clicked block is a chalice
        if (!(level.getBlockEntity(clickedPos) instanceof ChaliceBlockEntity entity))
        {
            return false;
        }

        // Check if player has enough blood
        if (!Capabilities.hasBlood(player, BLOOD_TRANSFER_AMOUNT))
        {
            // Optional: Play a sound or show a message indicating not enough blood
            return false;
        }

        // Simulate the fluid insertion to check if chalice can accept the blood
        int canInsert = entity.insertFluid(BLOOD_TRANSFER_AMOUNT, true);
        if (canInsert <= 0)
        {
            // Chalice is full or can't accept fluid
            // Optional: Play a sound or show a message indicating chalice is full
            return false;
        }

        // Perform the actual transfer
        // Use the amount that can actually be inserted (might be less than requested if chalice is nearly full)
        int actuallyInserted = entity.insertFluid(canInsert, false);

        if (actuallyInserted > 0)
        {
            // Remove the same amount from the player
            Capabilities.removeBlood(player, actuallyInserted);

            // Optional: Play blood transfer sound/particles
            // level.playSound(null, context.getClickedPos(), ModSounds.BLOOD_TRANSFER.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            return true;
        }

        return false;
    }

    /**
     * Extract blood from chalice to player using a dagger
     * @param player The player
     * @param level The world
     * @param context The use context
     * @return true if blood was successfully extracted
     */
    public static boolean extractBloodFromChalice(Player player, Level level, UseOnContext context)
    {
        // Check if player is holding a dagger
        if (!player.getMainHandItem().is(ModItems.DAGGER.get()))
        {
            return false;
        }

        // Check if the clicked block is a chalice
        if (!(level.getBlockEntity(context.getClickedPos()) instanceof ChaliceBlockEntity entity))
        {
            return false;
        }

        // Simulate extraction
        int canExtract = entity.extractFluid(BLOOD_TRANSFER_AMOUNT, true);
        if (canExtract <= 0)
        {
            // Chalice is empty
            return false;
        }

        // Perform the actual extraction
        int actuallyExtracted = entity.extractFluid(canExtract, false);

        if (actuallyExtracted > 0)
        {
            // Add the blood to the player
            Capabilities.addBlood(player, actuallyExtracted);

            // Optional: Play sound/particles
            // level.playSound(null, context.getClickedPos(), ModSounds.BLOOD_TRANSFER.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            return true;
        }

        return false;
    }
}