package com.nitro.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Box;

/** 2D drawing helpers (everything is drawn through DrawContext.fill so it works on any 1.21.x). */
public final class Gfx {
    private Gfx() {}

    public static int tw(String s) { return MinecraftClient.getInstance().textRenderer.getWidth(s); }

    public static void text(DrawContext c, String s, int x, int y, int col) {
        if ((col >>> 24) == 0) col |= 0xFF000000;
        c.drawText(MinecraftClient.getInstance().textRenderer, s, x, y, col, true);
    }

    public static void rrect(DrawContext c, int x, int y, int w, int h, int col) {
        if (w < 3 || h < 3) { c.fill(x, y, x + w, y + h, col); return; }
        c.fill(x + 1, y, x + w - 1, y + h, col);
        c.fill(x, y + 1, x + 1, y + h - 1, col);
        c.fill(x + w - 1, y + 1, x + w, y + h - 1, col);
    }

    /** "Glass" panel: translucent dark body, accent top edge, soft highlights. */
    public static void glass(DrawContext c, int x, int y, int w, int h) {
        rrect(c, x, y, w, h, Theme.bg());
        c.fill(x + 2, y, x + w - 2, y + 1, Theme.accentA(0xA0));
        c.fill(x + 2, y + 1, x + w - 2, y + 2, 0x14FFFFFF);
        c.fill(x + 2, y + h - 1, x + w - 2, y + h, 0x30FFFFFF);
        c.fill(x, y + 2, x + 1, y + h - 2, 0x28FFFFFF);
        c.fill(x + w - 1, y + 2, x + w, y + h - 2, 0x28FFFFFF);
    }

    public static void outline(DrawContext c, int x1, int y1, int x2, int y2, int col) {
        c.fill(x1, y1, x2, y1 + 1, col); c.fill(x1, y2 - 1, x2, y2, col);
        c.fill(x1, y1, x1 + 1, y2, col); c.fill(x2 - 1, y1, x2, y2, col);
    }

    public static void circle(DrawContext c, int cx, int cy, int r, int col) {
        for (int dy = -r; dy <= r; dy++) {
            int half = (int) Math.sqrt(r * r - dy * dy);
            c.fill(cx - half, cy + dy, cx + half + 1, cy + dy + 1, col);
        }
    }

    public static void ring(DrawContext c, int cx, int cy, int r, int t, int col) {
        for (int dy = -r; dy <= r; dy++) {
            int oh = (int) Math.sqrt(r * r - dy * dy);
            int ir = r - t;
            if (Math.abs(dy) >= ir) { c.fill(cx - oh, cy + dy, cx + oh + 1, cy + dy + 1, col); continue; }
            int ih = (int) Math.sqrt(ir * ir - dy * dy);
            c.fill(cx - oh, cy + dy, cx - ih, cy + dy + 1, col);
            c.fill(cx + ih + 1, cy + dy, cx + oh + 1, cy + dy + 1, col);
        }
    }

    /** Line of squares, clipped to the screen (Liang-Barsky). */
    public static void line(DrawContext c, double x1, double y1, double x2, double y2, int w, int col) {
        double xmin = -8, ymin = -8, xmax = c.getScaledWindowWidth() + 8, ymax = c.getScaledWindowHeight() + 8;
        double dx = x2 - x1, dy = y2 - y1, t0 = 0, t1 = 1;
        double[] p = {-dx, dx, -dy, dy};
        double[] q = {x1 - xmin, xmax - x1, y1 - ymin, ymax - y1};
        for (int i = 0; i < 4; i++) {
            if (p[i] == 0) { if (q[i] < 0) return; }
            else {
                double t = q[i] / p[i];
                if (p[i] < 0) { if (t > t1) return; if (t > t0) t0 = t; }
                else { if (t < t0) return; if (t < t1) t1 = t; }
            }
        }
        double ax = x1 + t0 * dx, ay = y1 + t0 * dy, bx = x1 + t1 * dx, by = y1 + t1 * dy;
        int wi = Math.max(1, w);
        double len = Math.hypot(bx - ax, by - ay);
        if (Double.isNaN(len)) return;
        int n = (int) Math.min(2500, Math.max(1, Math.ceil(len / wi)));
        for (int i = 0; i <= n; i++) {
            double t = i / (double) n;
            int px = (int) Math.round(ax + (bx - ax) * t - wi / 2.0);
            int py = (int) Math.round(ay + (by - ay) * t - wi / 2.0);
            c.fill(px, py, px + wi, py + wi, col);
        }
    }

    /** 3D wireframe box projected onto the HUD (visible through walls). */
    public static void wire(DrawContext c, Box b, int col, int w) {
        double[][] v = {
            {b.minX, b.minY, b.minZ}, {b.maxX, b.minY, b.minZ}, {b.maxX, b.minY, b.maxZ}, {b.minX, b.minY, b.maxZ},
            {b.minX, b.maxY, b.minZ}, {b.maxX, b.maxY, b.minZ}, {b.maxX, b.maxY, b.maxZ}, {b.minX, b.maxY, b.maxZ}};
        int[][] e = {{0,1},{1,2},{2,3},{3,0},{4,5},{5,6},{6,7},{7,4},{0,4},{1,5},{2,6},{3,7}};
        for (int[] ed : e) Proj.line3(c, v[ed[0]], v[ed[1]], col, w);
    }

    public static int alpha(int rgb, int a) { return (a << 24) | (rgb & 0xFFFFFF); }
    public static int lerpColor(int c1, int c2, float t) {
        int r = (int) (((c1 >> 16) & 255) * (1 - t) + ((c2 >> 16) & 255) * t);
        int g = (int) (((c1 >> 8) & 255) * (1 - t) + ((c2 >> 8) & 255) * t);
        int b = (int) ((c1 & 255) * (1 - t) + (c2 & 255) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static void border(DrawContext c, int x, int y, int w, int h, int col) {
        c.fill(x + 1, y, x + w - 1, y + 1, col); c.fill(x + 1, y + h - 1, x + w - 1, y + h, col);
        c.fill(x, y + 1, x + 1, y + h - 1, col); c.fill(x + w - 1, y + 1, x + w, y + h - 1, col);
    }

    public static void rgrad(DrawContext c, int x, int y, int w, int h, int top, int bot) {
        c.fillGradient(x + 1, y, x + w - 1, y + h, top, bot);
        c.fillGradient(x, y + 1, x + 1, y + h - 1, top, bot);
        c.fillGradient(x + w - 1, y + 1, x + w, y + h - 1, top, bot);
    }

    public static int bright() { return lerpColor(Theme.accent(), 0xFFFFFFFF, 0.28f); }
    public static int deep() { return lerpColor(Theme.accent(), 0xFF000000, 0.40f); }

    public static void bold(DrawContext c, String s, int x, int y, int col) {
        MinecraftClient mc = MinecraftClient.getInstance();
        c.drawText(mc.textRenderer, s, x, y, col | 0xFF000000, false);
        c.drawText(mc.textRenderer, s, x + 1, y, col | 0xFF000000, false);
    }

    public static void ctext(DrawContext c, String s, int cx, int y, int col) { text(c, s, cx - tw(s) / 2, y, col); }

    public static String fit(String s, int maxW) {
        if (tw(s) <= maxW) return s;
        while (s.length() > 2 && tw(s + "..") > maxW) s = s.substring(0, s.length() - 1);
        return s + "..";
    }

    /** Glass card used by HUD elements: dark body + soft border. */
    public static void card(DrawContext c, int x, int y, int w, int h) {
        rrect(c, x, y, w, h, 0xE00C0814);
        border(c, x, y, w, h, Theme.accentA(0x55));
        c.fill(x + 2, y + 1, x + w - 2, y + 2, 0x14FFFFFF);
    }
}
