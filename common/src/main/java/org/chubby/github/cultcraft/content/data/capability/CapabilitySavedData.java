package org.chubby.github.cultcraft.content.data.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.chubby.github.cultcraft.api.capability.CapabilityAttachment;

import java.util.UUID;

/**
 * Persistent storage for all player capabilities
 * Automatically saves and loads capability data
 */
public class CapabilitySavedData extends SavedData {

    private static final String DATA_NAME = "cultcraft_capabilities";

    /**
     * Save all player capabilities to NBT
     */
    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag playersTag = new CompoundTag();

        for (UUID playerUUID : CapabilityAttachment.getAllPlayerUUIDs()) {
            CompoundTag playerData = CapabilityAttachment.savePlayerData(playerUUID);
            if (!playerData.isEmpty()) {
                playersTag.put(playerUUID.toString(), playerData);
            }
        }

        tag.put("players", playersTag);
        return tag;
    }

    /**
     * Load all player capabilities from NBT
     */
    public static CapabilitySavedData load(CompoundTag tag) {
        CapabilitySavedData data = new CapabilitySavedData();

        if (tag.contains("players")) {
            CompoundTag playersTag = tag.getCompound("players");

            for (String uuidString : playersTag.getAllKeys()) {
                try {
                    UUID playerUUID = UUID.fromString(uuidString);
                    CompoundTag playerData = playersTag.getCompound(uuidString);
                    CapabilityAttachment.loadPlayerData(playerUUID, playerData);
                } catch (IllegalArgumentException e) {
                    // Invalid UUID, skip
                }
            }
        }

        return data;
    }

    /**
     * Get or create the saved data for a level
     */
    public static CapabilitySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                CapabilitySavedData::load,
                CapabilitySavedData::new,
                DATA_NAME
        );
    }

    /**
     * Mark data as dirty (needs saving)
     */
    public static void markDirty(ServerLevel level) {
        CapabilitySavedData data = get(level);
        data.setDirty();
    }
}