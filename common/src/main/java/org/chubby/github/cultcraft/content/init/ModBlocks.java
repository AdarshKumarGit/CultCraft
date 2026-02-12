package org.chubby.github.cultcraft.content.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.chubby.github.cultcraft.Constants;
import org.chubby.github.cultcraft.CultCraft;
import org.chubby.github.cultcraft.blocks.custom.ChaliceBlock;

public class ModBlocks
{
    public static final DeferredRegister<Block> REGISTRAR = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> CHALICE = REGISTRAR.register(CultCraft.loc("chalice"),()->new ChaliceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
}
