package org.chubby.github.cultcraft.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ModHelper {

    /**
     * Returns a deterministic gradient color.
     *
     * @param startColor RGB hex (0xRRGGBB)
     * @param endColor   RGB hex (0xRRGGBB)
     * @param t          progress [0.0 – 1.0]
     */
    public static int gradientColor(int startColor, int endColor, float t) {
        t = Math.max(0f, Math.min(1f, t));

        int r = (int)(((startColor >> 16 & 0xFF) * (1 - t)) + ((endColor >> 16 & 0xFF) * t));
        int g = (int)(((startColor >> 8  & 0xFF) * (1 - t)) + ((endColor >> 8  & 0xFF) * t));
        int b = (int)(((startColor       & 0xFF) * (1 - t)) + ((endColor       & 0xFF) * t));

        return (r << 16) | (g << 8) | b;
    }

    public static void drawGradientText(
            GuiGraphics gui,
            Font font,
            String text,
            int x,
            int y,
            int startColor,
            int endColor
    ) {
        int length = text.length();

        for (int i = 0; i < length; i++) {
            float t = length == 1 ? 0f : i / (float)(length - 1);
            int color = gradientColor(startColor, endColor, t);

            String c = String.valueOf(text.charAt(i));
            gui.drawString(font, c, x, y, color);
            x += font.width(c);
        }
    }

}
