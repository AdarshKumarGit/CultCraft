package org.chubby.github.cultcraft.util;

import net.minecraft.world.entity.player.Player;
import org.chubby.github.cultcraft.api.capability.CapabilityAttachment;
import org.chubby.github.cultcraft.api.capability.CapabilityProvider;
import org.chubby.github.cultcraft.api.capability.ICapability;
import org.chubby.github.cultcraft.content.capability.BloodCap;
import org.chubby.github.cultcraft.content.capability.PlayerAlignmentCap;
import org.jetbrains.annotations.Nullable;


/**
 * Helper class for easy capability access
 * Provides static methods to get capabilities from players
 */
public class Capabilities {

    /**
     * Get a capability from a player
     * @param player The player
     * @param capabilityId The capability identifier
     * @param clazz The capability class
     * @return The capability instance, or null if not available
     */
    @Nullable
    public static <T extends ICapability> T get(Player player, String capabilityId, Class<T> clazz) {
        CapabilityProvider provider = CapabilityAttachment.get(player);
        if (provider == null) {
            return null;
        }

        try {
            return provider.getCapability(capabilityId, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get blood capability from player
     */
    @Nullable
    public static BloodCap getBlood(Player player) {
        return get(player, BloodCap.ID, BloodCap.class);
    }

    /**
     * Get blood amount from player
     * @return Blood amount, or 0 if capability not available
     */
    public static int getBloodAmount(Player player) {
        BloodCap blood = getBlood(player);
        return blood != null ? blood.getBloodAmt() : 0;
    }

    /**
     * Check if player has at least the specified blood amount
     */
    public static boolean hasBlood(Player player, int amount) {
        BloodCap blood = getBlood(player);
        return blood != null && blood.hasBlood(amount);
    }

    /**
     * Add blood to player
     * @return true if successful, false if capability not available
     */
    public static boolean addBlood(Player player, int amount) {
        BloodCap blood = getBlood(player);
        if (blood != null) {
            blood.addBlood(amount);
            return true;
        }
        return false;
    }

    /**
     * Remove blood from player
     * @return true if successful, false if capability not available
     */
    public static boolean removeBlood(Player player, int amount) {
        BloodCap blood = getBlood(player);
        if (blood != null) {
            blood.removeBlood(amount);
            return true;
        }
        return false;
    }

    /**
     * Set blood amount for player
     * @return true if successful, false if capability not available
     */
    public static boolean setBlood(Player player, int amount) {
        BloodCap blood = getBlood(player);
        if (blood != null) {
            blood.setBloodAmt(amount);
            return true;
        }
        return false;
    }

    public static PlayerAlignmentCap getAlignmentCap(Player player)
    {
        return get(player,"cultcraft:player_alignment", PlayerAlignmentCap.class);
    }

    public static int getPlayerAlignment(Player player)
    {
        return getAlignmentCap(player).getAlignmentLevel();
    }

    public static boolean increaseAlignment(Player player, int level)
    {
        PlayerAlignmentCap cap = getAlignmentCap(player);
        if(cap != null)
        {
            cap.increaseAlignment(level);
            return true;
        }
        return false;
    }

    public static boolean decreaseAlignment(Player player, int level)
    {
        PlayerAlignmentCap cap = getAlignmentCap(player);
        if(cap != null)
        {
            cap.decreaseAlignment(level);
            return true;
        }
        return false;
    }

    public static boolean isDivine(Player player)
    {
        PlayerAlignmentCap cap = getAlignmentCap(player);
        if(cap != null)
        {
            return cap.alignmentLevel > 600;
        }
        return false;
    }

    public static boolean isDevil(Player player)
    {
        PlayerAlignmentCap cap = getAlignmentCap(player);
        if(cap != null)
        {
            return cap.alignmentLevel < -600;
        }
        return false;
    }
}