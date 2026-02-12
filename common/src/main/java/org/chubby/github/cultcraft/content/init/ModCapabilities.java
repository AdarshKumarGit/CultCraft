package org.chubby.github.cultcraft.content.init;

import dev.architectury.event.events.common.LifecycleEvent;
import org.chubby.github.cultcraft.api.capability.CapabilityAttachment;
import org.chubby.github.cultcraft.api.capability.CapabilityProvider;
import org.chubby.github.cultcraft.content.capability.BloodCap;
import org.chubby.github.cultcraft.content.capability.PlayerAlignmentCap;
import org.chubby.github.cultcraft.content.data.capability.CapabilitySavedData;

public class ModCapabilities
{
    public static void register()
    {
        CapabilityProvider.registerCapability("cultcraft:blood", BloodCap::new);
        CapabilityProvider.registerCapability("cultcraft:player_alignment", PlayerAlignmentCap::new);

        CapabilityAttachment.init();

        LifecycleEvent.SERVER_LEVEL_LOAD.register(CapabilitySavedData::get);

        LifecycleEvent.SERVER_LEVEL_SAVE.register(CapabilitySavedData::markDirty);
    }
}
