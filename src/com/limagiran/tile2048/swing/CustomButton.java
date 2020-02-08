package com.limagiran.tile2048.swing;

import com.limagiran.tile2048.Constants;
import com.limagiran.tile2048.RendererUtils;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author Vinicius Silva
 */
public class CustomButton extends JButton implements Constants {

    public CustomButton() {
        init();
    }

    public CustomButton(Icon icon) {
        super(icon);
        init();
    }

    public CustomButton(String text) {
        super(text);
        init();
    }

    public CustomButton(Action a) {
        super(a);
        init();
    }

    public CustomButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    private void init() {
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setFont(FONT.deriveFont(Font.BOLD, 16f));
        setBackground(FOREGROUND);
        setForeground(FOREGROUND_LIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RendererUtils.setupGraphics(g2);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);

        String str = getText();
        if (str.isEmpty()) {
            return;
        }
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int xStr = (getWidth() - fm.stringWidth(str)) / 2;
        int yStr = fm.getAscent() + fm.getLeading() + ((getHeight() - fm.getHeight()) / 2);
        g2.drawString(str, xStr, yStr);
    }

}
