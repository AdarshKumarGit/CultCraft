package org.chubby.github.cultcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.chubby.github.cultcraft.Constants;
import org.chubby.github.cultcraft.api.capability.CapabilityAttachment;
import org.chubby.github.cultcraft.api.capability.CapabilityProvider;
import org.chubby.github.cultcraft.api.capability.ICapability;

import java.util.function.Supplier;

/**
 * Packet for synchronizing capability data from server to client
 */
public class SyncCapabilityPacket {

    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "sync_capability");

    private final String capabilityId;
    private final CompoundTag data;

    public SyncCapabilityPacket(String capabilityId, CompoundTag data) {
        this.capabilityId = capabilityId;
        this.data = data;
    }

    public SyncCapabilityPacket(ICapability capability) {
        this.capabilityId = capability.getCapabilityId();
        this.data = capability.serialize();
    }

    /**
     * Encode packet to buffer
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(capabilityId);
        buf.writeNbt(data);
    }

    /**
     * Decode packet from buffer
     */
    public static SyncCapabilityPacket decode(FriendlyByteBuf buf) {
        String capabilityId = buf.readUtf();
        CompoundTag data = buf.readNbt();
        return new SyncCapabilityPacket(capabilityId, data);
    }

    /**
     * Handle packet on client side
     */
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            CapabilityProvider provider = CapabilityAttachment.getClientCapability();

            // Get or create the capability on client side
            try {
                ICapability capability = provider.getCapability(capabilityId, ICapability.class);
                capability.deserialize(data);
            } catch (Exception e) {
                Constants.LOGGER.error("Failed to sync capability: " + capabilityId, e);
            }
        });
    }

    /**
     * Register packet
     */
    public static void register() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                ID,
                (buf, context) -> {
                    SyncCapabilityPacket packet = decode(buf);
                    packet.handle(context);
                }
        );
    }

    public void apply(Supplier<NetworkManager.PacketContext> packetContextSupplier) {
    }
}