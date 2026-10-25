package com.nitro.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.render.RenderTickCounter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.UUID;

/**
 * Every Minecraft/authlib call that changed name between 1.21.x releases lives here.
 * If the build fails, the error will most likely point at one of these lines - just rename the call.
 */
public final class Compat {
    private Compat() {}
    public static float tickProgress(RenderTickCounter c) { return c.getTickProgress(false); }
    public static String name(GameProfile p) { return p.name(); }
    public static UUID id(GameProfile p) { return p.id(); }

    /** Text drawn at a scale (1.21.6+ uses a 2D matrix stack). If this fails to compile, replace body with c.drawText(...) unscaled. */
    public static void scaledText(DrawContext c, String s, float x, float y, float sc, int col, boolean shadow) {
        var m = c.getMatrices();
        m.pushMatrix();
        m.translate(x, y);
        m.scale(sc, sc);
        c.drawText(MinecraftClient.getInstance().textRenderer, s, 0, 0, col | 0xFF000000, shadow);
        m.popMatrix();
    }
}
