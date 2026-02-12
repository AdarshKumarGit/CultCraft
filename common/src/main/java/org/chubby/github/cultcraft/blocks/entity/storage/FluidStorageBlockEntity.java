package org.chubby.github.cultcraft.blocks.entity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.chubby.github.cultcraft.api.fluid.FluidStorage;
import org.jetbrains.annotations.Nullable;

public abstract class FluidStorageBlockEntity extends BlockEntity {

    private final FluidStorage storage;

    public FluidStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.storage = createFluidStorage();
    }

    /**
     * Override this method to define the fluid storage capacity and transfer rates
     * @return A new FluidStorage instance
     */
    protected abstract FluidStorage createFluidStorage();

    /**
     * Get the fluid storage for this block entity
     */
    public FluidStorage getFluidStorage() {
        return storage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveFluidStorage(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadFluidStorage(tag);
    }

    /**
     * Save fluid storage data to NBT
     */
    protected void saveFluidStorage(CompoundTag tag) {
        CompoundTag fluidTag = new CompoundTag();
        fluidTag.putInt("FluidAmount", storage.getFluidAmount());
        fluidTag.putInt("MaxFluidAmount", storage.getMaxFluidAmount());
        fluidTag.putInt("MaxReceive", storage.getMaxReceive());
        fluidTag.putInt("MaxInsert", storage.getMaxInsert());
        tag.put("FluidStorage", fluidTag);
    }

    /**
     * Load fluid storage data from NBT
     */
    protected void loadFluidStorage(CompoundTag tag) {
        if (tag.contains("FluidStorage")) {
            CompoundTag fluidTag = tag.getCompound("FluidStorage");
            storage.setFluidAmount(fluidTag.getInt("FluidAmount"));
        }
    }

    /**
     * Get update tag for client synchronization
     */
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveFluidStorage(tag);
        return tag;
    }

    /**
     * Get update packet for client synchronization
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Mark this block entity as changed and sync to clients
     */
    public void markUpdated() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    /**
     * Helper method to insert fluid
     */
    public int insertFluid(int amount, boolean simulate) {
        int inserted = storage.insertFluid(amount, simulate);
        if (inserted > 0 && !simulate) {
            markUpdated();
        }
        return inserted;
    }

    /**
     * Helper method to extract fluid
     */
    public int extractFluid(int amount, boolean simulate) {
        int extracted = storage.extractFluid(amount, simulate);
        if (extracted > 0 && !simulate) {
            markUpdated();
        }
        return extracted;
    }
}