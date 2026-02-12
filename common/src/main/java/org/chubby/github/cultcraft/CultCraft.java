package org.chubby.github.cultcraft;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.resources.ResourceLocation;
import org.chubby.github.cultcraft.content.init.*;
import org.chubby.github.cultcraft.content.ritual.RitualEventHandler;
import org.chubby.github.cultcraft.core.commands.DebugCommands;
import org.chubby.github.cultcraft.networking.NetworkHandler;
import org.chubby.github.cultcraft.util.ModEventHandler;

public final class CultCraft {
    public static final String MOD_ID = "cultcraft";

    public static void init() {
        //Registries
        ModItems.REGISTRAR.register();
        ModBlocks.REGISTRAR.register();
        ModBlockEntity.REGISTRAR.register();
        ModCapabilities.register();
        NetworkHandler.init();
        ModRituals.init();

        //Events
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            DebugCommands.register(dispatcher);
        });
        RitualEventHandler.register();
        ModEventHandler.register();
    }

    public static ResourceLocation loc(String path)
    {
        return new ResourceLocation(Constants.MOD_ID,path);
    }
}
