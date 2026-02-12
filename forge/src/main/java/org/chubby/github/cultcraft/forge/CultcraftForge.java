package org.chubby.github.cultcraft.forge;

import org.chubby.github.cultcraft.CultCraft;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.chubby.github.cultcraft.client.CultCraftClient;

@Mod(CultCraft.MOD_ID)
public final class CultcraftForge {
    public CultcraftForge() {
        EventBuses.registerModEventBus(CultCraft.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        CultCraft.init();
        CultCraftClient.init();
    }
}
