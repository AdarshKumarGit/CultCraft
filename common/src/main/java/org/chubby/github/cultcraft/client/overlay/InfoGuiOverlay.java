package org.chubby.github.cultcraft.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.chubby.github.cultcraft.Constants;
import org.chubby.github.cultcraft.util.Capabilities;
import org.chubby.github.cultcraft.content.capability.BloodCap;
import org.chubby.github.cultcraft.util.ModHelper;

import java.util.Objects;

public class InfoGuiOverlay
{
    public static void register() {
        //ClientGuiEvent.RENDER_HUD.register(InfoGuiOverlay::render);
    }

    private static void render(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int x = 5;
        int y = 5;

        guiGraphics.drawString(
                mc.font,
                Constants.MOD_NAME,
                x,
                y,
                ModHelper.gradientColor(0xFFFF55, 0xFFAA00, partialTick * 0.5f),
                false
        );
        guiGraphics.drawString(
                mc.font,
                Constants.MOD_VERSION,
                x,
                y+10,
                ModHelper.gradientColor(0xFFFF55, 0xFFAA00, partialTick * 0.5f),
                false
        );
        guiGraphics.drawString(
                mc.font,
                Component.literal("Blood : "+ Objects.requireNonNull(Capabilities.get(mc.player, "cultcraft:blood", BloodCap.class)).getBloodAmt()),
                x,
                y+25,
                ModHelper.gradientColor(0xFFFF55, 0xFFAA00, partialTick * 0.5f),
                false
        );
    }
}
