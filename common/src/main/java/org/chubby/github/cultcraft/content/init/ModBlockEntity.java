package org.chubby.github.cultcraft.content.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.chubby.github.cultcraft.Constants;
import org.chubby.github.cultcraft.CultCraft;
import org.chubby.github.cultcraft.blocks.entity.ChaliceBlockEntity;

public class ModBlockEntity
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTRAR = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<ChaliceBlockEntity>> CHALICE_BE = REGISTRAR
            .register(CultCraft.loc("chalice_be"),() -> BlockEntityType.Builder.of(ChaliceBlockEntity::new,ModBlocks.CHALICE.get()).build(null));
}
