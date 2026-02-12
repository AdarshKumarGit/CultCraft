package org.chubby.github.cultcraft.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages all capabilities attached to a player
 * Handles registration, retrieval, serialization, and synchronization
 */
public class CapabilityProvider {

    private final Map<String, ICapability> capabilities = new HashMap<>();
    private static final Map<String, Supplier<? extends ICapability>> CAPABILITY_FACTORIES = new HashMap<>();

    /**
     * Register a capability type with its factory
     * @param id Unique capability identifier
     * @param factory Supplier that creates new instances
     */
    public static <T extends ICapability> void registerCapability(String id, Supplier<T> factory) {
        CAPABILITY_FACTORIES.put(id, factory);
    }

    /**
     * Get or create a capability instance
     * @param capabilityId The capability identifier
     * @return The capability instance, or null if not registered
     */
    @SuppressWarnings("unchecked")
    public <T extends ICapability> T getCapability(String capabilityId, Class<T> clazz) {
        return (T) capabilities.computeIfAbsent(capabilityId, id -> {
            Supplier<? extends ICapability> factory = CAPABILITY_FACTORIES.get(id);
            if (factory == null) {
                throw new IllegalArgumentException("Capability not registered: " + id);
            }
            return factory.get();
        });
    }

    /**
     * Check if a capability is present
     */
    public boolean hasCapability(String capabilityId) {
        return capabilities.containsKey(capabilityId);
    }

    /**
     * Serialize all capabilities to NBT
     */
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        capabilities.forEach((id, capability) -> {
            tag.put(id, capability.serialize());
        });
        return tag;
    }

    /**
     * Deserialize all capabilities from NBT
     */
    public void deserialize(CompoundTag tag) {
        tag.getAllKeys().forEach(key -> {
            Supplier<? extends ICapability> factory = CAPABILITY_FACTORIES.get(key);
            if (factory != null) {
                ICapability capability = factory.get();
                capability.deserialize(tag.getCompound(key));
                capabilities.put(key, capability);
            }
        });
    }

    /**
     * Copy all capabilities from another provider
     * Used when player respawns
     */
    public void copyFrom(CapabilityProvider other) {
        other.capabilities.forEach((id, otherCap) -> {
            ICapability thisCap = capabilities.computeIfAbsent(id, key -> {
                Supplier<? extends ICapability> factory = CAPABILITY_FACTORIES.get(key);
                return factory != null ? factory.get() : null;
            });
            if (thisCap != null) {
                thisCap.copyFrom(otherCap);
            }
        });
    }

    /**
     * Sync all dirty capabilities to the player
     */
    public void syncAll(ServerPlayer player) {
        capabilities.values().forEach(cap -> cap.sync(player));
    }

    /**
     * Force sync all capabilities regardless of dirty state
     */
    public void forceSyncAll(ServerPlayer player) {
        capabilities.values().forEach(cap -> {
            if (cap instanceof PlayerCapability pc) {
                pc.forceSync(player);
            } else {
                cap.sync(player);
            }
        });
    }
}