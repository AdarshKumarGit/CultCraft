package org.chubby.github.cultcraft.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.chubby.github.cultcraft.networking.NetworkHandler;

/**
 * Abstract base class for player-attached capabilities
 * Provides common functionality for all capability implementations
 */
public abstract class PlayerCapability implements ICapability {

    protected boolean dirty = false;

    /**
     * Mark this capability as needing synchronization
     */
    public void markDirty() {
        this.dirty = true;
    }

    /**
     * Check if capability needs synchronization
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Reset dirty flag after synchronization
     */
    public void clearDirty() {
        this.dirty = false;
    }

    @Override
    public void sync(ServerPlayer player) {
        if (isDirty()) {
            NetworkHandler.sendToPlayer(player, this);
            clearDirty();
        }
    }

    /**
     * Force sync regardless of dirty state
     */
    public void forceSync(ServerPlayer player) {
        NetworkHandler.sendToPlayer(player, this);
        clearDirty();
    }

    /**
     * Sync to all tracking players
     * Useful for capabilities that affect visible player state
     */
    public void syncToTracking(ServerPlayer player) {
        NetworkHandler.sendToTracking(player, this);
        clearDirty();
    }

    @Override
    public abstract String getCapabilityId();

    @Override
    public abstract CompoundTag serialize();

    @Override
    public abstract void deserialize(CompoundTag tag);

    @Override
    public abstract void copyFrom(ICapability other);
}