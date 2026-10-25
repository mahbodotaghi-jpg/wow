package com.nitro.core;

import net.minecraft.client.gui.DrawContext;

/** Tiny vector icons drawn from fills/lines (no textures needed). */
public final class Icons {
    private Icons() {}

    public static void cat(DrawContext c, Category cat, int cx, int cy, int col) {
        switch (cat) {
            case COMBAT -> { Gfx.line(c, cx - 5, cy - 5, cx + 5, cy + 5, 1, col); Gfx.line(c, cx + 5, cy - 5, cx - 5, cy + 5, 1, col);
                             Gfx.line(c, cx - 7, cy - 2, cx - 2, cy - 7, 1, col); Gfx.line(c, cx + 7, cy - 2, cx + 2, cy - 7, 1, col); }
            case VISUALS -> eye(c, cx, cy, col);
            case UNSEEN -> { Gfx.circle(c, cx, cy - 2, 4, col); c.fill(cx - 4, cy - 2, cx + 5, cy + 5, col); c.fill(cx - 2, cy + 3, cx - 1, cy + 6, 0xFF000000 | 0x0B0716); c.fill(cx + 1, cy + 3, cx + 2, cy + 6, 0xFF000000 | 0x0B0716); }
            case HUD -> { c.fill(cx - 5, cy - 5, cx - 1, cy - 1, col); c.fill(cx + 1, cy - 5, cx + 5, cy - 1, col);
                          c.fill(cx - 5, cy + 1, cx - 1, cy + 5, col); c.fill(cx + 1, cy + 1, cx + 5, cy + 5, col); }
            default -> { Gfx.ring(c, cx, cy, 5, 2, col); c.fill(cx - 1, cy - 8, cx + 1, cy - 5, col); c.fill(cx - 1, cy + 5, cx + 1, cy + 8, col);
                         c.fill(cx - 8, cy - 1, cx - 5, cy + 1, col); c.fill(cx + 5, cy - 1, cx + 8, cy + 1, col); }
        }
    }

    public static void eye(DrawContext c, int cx, int cy, int col) {
        Gfx.line(c, cx - 7, cy, cx - 3, cy - 3, 1, col); Gfx.line(c, cx - 3, cy - 3, cx + 3, cy - 3, 1, col); Gfx.line(c, cx + 3, cy - 3, cx + 7, cy, 1, col);
        Gfx.line(c, cx - 7, cy, cx - 3, cy + 3, 1, col); Gfx.line(c, cx - 3, cy + 3, cx + 3, cy + 3, 1, col); Gfx.line(c, cx + 3, cy + 3, cx + 7, cy, 1, col);
        Gfx.circle(c, cx, cy, 2, col);
    }

    public static void sliders(DrawContext c, int cx, int cy, int col) {
        c.fill(cx - 5, cy - 4, cx + 5, cy - 3, col); c.fill(cx - 5, cy, cx + 5, cy + 1, col); c.fill(cx - 5, cy + 4, cx + 5, cy + 5, col);
        c.fill(cx - 2, cy - 6, cx, cy - 1, col); c.fill(cx + 2, cy - 2, cx + 4, cy + 3, col); c.fill(cx - 3, cy + 2, cx - 1, cy + 7, col);
    }

    public static void search(DrawContext c, int cx, int cy, int col) {
        Gfx.ring(c, cx - 1, cy - 1, 4, 1, col); Gfx.line(c, cx + 2, cy + 2, cx + 5, cy + 5, 1, col);
    }

    public static void diamond(DrawContext c, int cx, int cy, int col) {
        Gfx.line(c, cx, cy - 4, cx + 4, cy, 1, col); Gfx.line(c, cx + 4, cy, cx, cy + 4, 1, col);
        Gfx.line(c, cx, cy + 4, cx - 4, cy, 1, col); Gfx.line(c, cx - 4, cy, cx, cy - 4, 1, col);
    }

    public static void shield(DrawContext c, int cx, int cy, int col) {
        c.fill(cx - 4, cy - 5, cx + 5, cy + 1, col);
        c.fill(cx - 3, cy + 1, cx + 4, cy + 3, col); c.fill(cx - 1, cy + 3, cx + 2, cy + 5, col);
    }
}
