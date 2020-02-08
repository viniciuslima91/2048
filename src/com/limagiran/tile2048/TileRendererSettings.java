package com.limagiran.tile2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Vinicius Silva
 */
public class TileRendererSettings {

    public final int value;
    public final String toString;
    public final Font font;
    public final Color fg;
    public final Color bg;
    public final int xStr;
    public final int yStr;

    public TileRendererSettings(int value, String toString, Font font, Color fg, Color bg, int xStr, int yStr) {
        this.value = value;
        this.toString = toString;
        this.font = font;
        this.fg = fg;
        this.bg = bg;
        this.xStr = xStr;
        this.yStr = yStr;
    }

    public static TileRendererSettings create(int value, Graphics2D g2) {
        Color fg = RendererUtils.getTileForegroundColor(value);
        Color bg = RendererUtils.getTileBackgroundColor(value);
        Font f = RendererUtils.getFont(value);
        FontMetrics fm = g2.getFontMetrics(f);
        String str = Integer.toString(value);
        int xStr = (Constants.TILE_PX - fm.stringWidth(str)) / 2;
        int yStr = fm.getAscent() + fm.getLeading() + ((Constants.TILE_PX - fm.getHeight()) / 2);
        return new TileRendererSettings(value, str, f, fg, bg, xStr, yStr);
    }

    public void paint(Graphics2D g2, int x, int y) {
        g2.setColor(bg);
        g2.fillRoundRect(x, y, Constants.TILE_PX, Constants.TILE_PX, 4, 4);
        g2.setColor(fg);
        g2.setFont(font);
        g2.drawString(toString, x + xStr, y + yStr);
    }
    
    public void paint(Graphics2D g2, Point p) {
        paint(g2, p.x, p.y);
    }

}
