package org.chubby.github.cultcraft.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.chubby.github.cultcraft.api.fluid.FluidStorage;
import org.chubby.github.cultcraft.blocks.entity.storage.FluidStorageBlockEntity;
import org.chubby.github.cultcraft.content.init.ModBlockEntity;

public class ChaliceBlockEntity extends FluidStorageBlockEntity {

    private static final int CHALICE_CAPACITY = 1000;

    private static final int TRANSFER_RATE = 100;

    public ChaliceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntity.CHALICE_BE.get(), pos, blockState);
    }

    @Override
    protected FluidStorage createFluidStorage() {
        return new FluidStorage(0, CHALICE_CAPACITY, TRANSFER_RATE, TRANSFER_RATE) {
            @Override
            protected void onContentsChanged() {
                markUpdated();
            }
        };
    }
}
