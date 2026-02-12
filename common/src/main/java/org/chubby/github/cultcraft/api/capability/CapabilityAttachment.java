package org.chubby.github.cultcraft.api.capability;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages capability attachment to players
 * Handles persistence across death and dimension changes
 */
public class CapabilityAttachment {

    private static final Map<UUID, CapabilityProvider> SERVER_CAPABILITIES = new HashMap<>();

    private static CapabilityProvider CLIENT_CAPABILITY = null;


    public static void init() {
        PlayerEvent.PLAYER_QUIT.register(player -> {

        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            CapabilityProvider provider = getOrCreate(player);
            provider.forceSyncAll(player);

        });

        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            if (!newPlayer.level().isClientSide) {
                CapabilityProvider oldProvider = get(oldPlayer);
                CapabilityProvider newProvider = getOrCreate((ServerPlayer) newPlayer);
                if (oldProvider != null) {
                    newProvider.copyFrom(oldProvider);
                    newProvider.forceSyncAll((ServerPlayer) newPlayer);
                }
            }
        });

        PlayerEvent.CHANGE_DIMENSION.register((serverPlayer, resourceKey, resourceKey1) -> {
            CapabilityProvider provider = get(serverPlayer);
            if (provider != null) {
                serverPlayer.level().getServer().tell(new net.minecraft.server.TickTask(
                        serverPlayer.level().getServer().getTickCount() + 1,
                        () -> provider.forceSyncAll(serverPlayer)
                ));
            }
        });
    }

    /**
     * Get capability provider for a player (server or client)
     */
    public static CapabilityProvider get(Player player) {
        if (player.level().isClientSide) {
            return CLIENT_CAPABILITY;
        } else {
            return SERVER_CAPABILITIES.get(player.getUUID());
        }
    }

    /**
     * Get or create capability provider for a server player
     */
    public static CapabilityProvider getOrCreate(ServerPlayer player) {
        return SERVER_CAPABILITIES.computeIfAbsent(
                player.getUUID(),
                uuid -> new CapabilityProvider()
        );
    }

    /**
     * Set client capability provider (called when receiving sync packet)
     */
    public static void setClientCapability(CapabilityProvider provider) {
        CLIENT_CAPABILITY = provider;
    }

    /**
     * Get client capability provider
     */
    public static CapabilityProvider getClientCapability() {
        if (CLIENT_CAPABILITY == null) {
            CLIENT_CAPABILITY = new CapabilityProvider();
        }
        return CLIENT_CAPABILITY;
    }

    /**
     * Remove player capabilities (when they log out)
     * Note: Don't call this directly - handled by SavedData system
     */
    public static void remove(UUID playerUUID) {
        SERVER_CAPABILITIES.remove(playerUUID);
    }

    /**
     * Load capabilities from NBT for a specific player
     */
    public static void loadPlayerData(UUID playerUUID, CompoundTag tag) {
        CapabilityProvider provider = new CapabilityProvider();
        provider.deserialize(tag);
        SERVER_CAPABILITIES.put(playerUUID, provider);
    }

    /**
     * Save capabilities to NBT for a specific player
     */
    public static CompoundTag savePlayerData(UUID playerUUID) {
        CapabilityProvider provider = SERVER_CAPABILITIES.get(playerUUID);
        return provider != null ? provider.serialize() : new CompoundTag();
    }

    /**
     * Get all player UUIDs with capability data
     */
    public static Iterable<UUID> getAllPlayerUUIDs() {
        return SERVER_CAPABILITIES.keySet();
    }
}