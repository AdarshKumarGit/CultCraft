package org.chubby.github.cultcraft.networking;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.chubby.github.cultcraft.api.capability.ICapability;
import org.chubby.github.cultcraft.networking.packet.SyncCapabilityPacket;

public class NetworkHandler {


    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation("cultcraft:messages"));

    public static void init() {
        SyncCapabilityPacket.register();
    }


    public static void sendToPlayer(ServerPlayer player, ICapability capability) {
        SyncCapabilityPacket packet = new SyncCapabilityPacket(capability);
        NetworkManager.sendToPlayer(player, SyncCapabilityPacket.ID, createBuffer(packet));
    }


    public static void sendToTracking(ServerPlayer player, ICapability capability) {
        SyncCapabilityPacket packet = new SyncCapabilityPacket(capability);
        FriendlyByteBuf buf = createBuffer(packet);

        NetworkManager.sendToPlayer(player, SyncCapabilityPacket.ID, buf);

        NetworkManager.sendToPlayers(
                player.getServer().getPlayerList().getPlayers().stream()
                        .filter(p -> p != player && p.distanceToSqr(player) < 4096) // 64 block radius
                        .toList(),
                SyncCapabilityPacket.ID,
                createBuffer(packet)
        );
    }


    public static void sendToAll(ICapability capability, ServerPlayer sourcePlayer) {
        SyncCapabilityPacket packet = new SyncCapabilityPacket(capability);
        NetworkManager.sendToPlayers(
                sourcePlayer.getServer().getPlayerList().getPlayers(),
                SyncCapabilityPacket.ID,
                createBuffer(packet)
        );
    }

    private static FriendlyByteBuf createBuffer(SyncCapabilityPacket packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(buf);
        return buf;
    }
}