package org.chubby.github.cultcraft.client;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import org.chubby.github.cultcraft.client.overlay.InfoGuiOverlay;

public class CultCraftClient {

    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register((minecraft) -> {
            InfoGuiOverlay.register();
        });
    }
}
