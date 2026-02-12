package org.chubby.github.cultcraft.content.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.chubby.github.cultcraft.Constants;
import org.chubby.github.cultcraft.CultCraft;
import org.chubby.github.cultcraft.items.CodexSanguinis;
import org.chubby.github.cultcraft.items.RitualPerformerItem;

import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTRAR =DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> DAGGER = registerItem("dagger",()->new Item(new Item.Properties().stacksTo(1)));
    public static final RegistrySupplier<Item> CODEX_SANGUINIS = registerItem("codex_sanguinis",()->new CodexSanguinis(new Item.Properties().stacksTo(1)));

    public static<T extends Item> RegistrySupplier<T> registerItem(String id, Supplier<T> item)
    {
        return REGISTRAR.register(CultCraft.loc(id),item);
    }
}
