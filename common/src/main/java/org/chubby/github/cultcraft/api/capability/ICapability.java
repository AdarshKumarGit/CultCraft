package org.chubby.github.cultcraft.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/**
 * Base interface for all capabilities
 * Capabilities handle data storage, serialization, and synchronization
 */
public interface ICapability {

    /**
     * Serialize capability data to NBT
     * @return CompoundTag containing all capability data
     */
    CompoundTag serialize();

    /**
     * Deserialize capability data from NBT
     * @param tag CompoundTag containing capability data
     */
    void deserialize(CompoundTag tag);

    /**
     * Copy data from another capability instance
     * Used when respawning players
     * @param other The capability to copy from
     */
    void copyFrom(ICapability other);

    /**
     * Sync capability data to the client
     * @param player The player to sync to
     */
    void sync(ServerPlayer player);

    /**
     * Get the unique identifier for this capability type
     * Used for packet routing and registration
     * @return Unique capability ID
     */
    String getCapabilityId();
}