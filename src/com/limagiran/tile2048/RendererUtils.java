package com.limagiran.tile2048;

import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vinicius Silva
 */
public class RendererUtils implements Constants {

    public static final Font FONT_BIG = FONT.deriveFont(Font.BOLD, 48f);
    public static final Font FONT_MEDIUM = FONT.deriveFont(Font.BOLD, 36f);
    public static final Font FONT_SMALL = FONT.deriveFont(Font.BOLD, 30f);

    private static final Map<Integer, Color> MAP_FG_COLOR = new HashMap<>();
    private static final Map<Integer, Color> MAP_BG_COLOR = new HashMap<>();

    static {
        MAP_BG_COLOR.put(0, new Color(205, 193, 180));
        MAP_BG_COLOR.put(2, new Color(238, 228, 218));
        MAP_BG_COLOR.put(4, new Color(237, 224, 200));
        MAP_BG_COLOR.put(8, new Color(242, 177, 121));
        MAP_BG_COLOR.put(16, new Color(245, 149, 99));
        MAP_BG_COLOR.put(32, new Color(246, 124, 95));
        MAP_BG_COLOR.put(64, new Color(246, 94, 59));
        MAP_BG_COLOR.put(128, new Color(237, 207, 114));
        MAP_BG_COLOR.put(256, new Color(237, 204, 97));
        MAP_BG_COLOR.put(512, new Color(237, 200, 80));
        MAP_BG_COLOR.put(1024, new Color(237, 197, 63));
        MAP_BG_COLOR.put(2048, new Color(237, 194, 46));
        MAP_BG_COLOR.put(4096, new Color(60, 58, 50));

        MAP_FG_COLOR.put(0, new Color(0, 0, 0, 0));
        MAP_FG_COLOR.put(2, new Color(119, 111, 102));
        MAP_FG_COLOR.put(4, new Color(119, 111, 102));
        MAP_FG_COLOR.put(8, new Color(249, 246, 242));
        MAP_FG_COLOR.put(16, new Color(249, 246, 242));
        MAP_FG_COLOR.put(32, new Color(249, 246, 242));
        MAP_FG_COLOR.put(64, new Color(249, 246, 242));
        MAP_FG_COLOR.put(128, new Color(249, 246, 242));
        MAP_FG_COLOR.put(256, new Color(249, 246, 242));
        MAP_FG_COLOR.put(512, new Color(249, 246, 242));
        MAP_FG_COLOR.put(1024, new Color(249, 246, 242));
        MAP_FG_COLOR.put(2048, new Color(249, 246, 242));
        MAP_FG_COLOR.put(4096, new Color(249, 246, 242));
    }

    public static Color getTileForegroundColor(int value) {
        Color c = MAP_FG_COLOR.get(value);
        if (c != null) {
            return c;
        }
        return MAP_FG_COLOR.get((value > 2048 ? 4096 : 0));
    }

    public static Color getTileBackgroundColor(int value) {
        Color c = MAP_BG_COLOR.get(value);
        if (c != null) {
            return c;
        }
        return MAP_BG_COLOR.get((value > 2048 ? 4096 : 0));
    }

    public static Font getFont(int value) {
        if (value < 128) {
            return FONT_BIG;
        }
        if (value < 1024) {
            return FONT_MEDIUM;
        }
        return FONT_SMALL;
    }

    private static final Map<Integer, TileRendererSettings> MAP_TILE_RENDERER_SETTINGS_CACHE = new HashMap<>();

    public static TileRendererSettings getTileRendererSettings(int value, Graphics2D g2) {
        TileRendererSettings set = MAP_TILE_RENDERER_SETTINGS_CACHE.get(value);
        if (set == null) {
            set = TileRendererSettings.create(value, g2);
            MAP_TILE_RENDERER_SETTINGS_CACHE.put(value, set);
        }
        return set;
    }

    public static void setupGraphics(Graphics2D g) {
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
    }
}
