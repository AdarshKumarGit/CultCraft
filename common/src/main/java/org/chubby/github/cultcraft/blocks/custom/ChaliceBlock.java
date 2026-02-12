package org.chubby.github.cultcraft.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.chubby.github.cultcraft.blocks.custom.storage.BaseFluidStorageBlock;
import org.chubby.github.cultcraft.blocks.entity.ChaliceBlockEntity;
import org.chubby.github.cultcraft.util.PlayerHelper;
import org.jetbrains.annotations.Nullable;

public class ChaliceBlock extends BaseFluidStorageBlock {

    public ChaliceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        boolean transferred = PlayerHelper.addBloodToChalice(player,level,pos);

        if(transferred){
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChaliceBlockEntity(pos,state);
    }
}
