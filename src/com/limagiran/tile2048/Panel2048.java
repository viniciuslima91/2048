package com.limagiran.tile2048;

import com.limagiran.tile2048.swing.CustomButton;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Vinicius Silva
 */
public class Panel2048 extends JPanel implements Constants {

    private final IPanel2048 model;

    public Panel2048(IPanel2048 model) {
        super(new BorderLayout(8, 8));
        this.model = model;
        init();
    }

    private void init() {
        setOpaque(true);
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(createHeaderPanel(), "First");
        add(new RendererPanel2048(model), "Center");
        add(createFooterPanel(), "Last");
        Thread t = new Thread(() -> {
            long delay = 1000 / 60; //60 fps;
            while (model.isRendererPanelAlive()) {
                repaint();
                Utils.sleep(delay);
            }
        });
        t.setDaemon(false);
        t.start();

    }

    private Component createHeaderPanel() {
        JLabel label = new JLabel("2048 Game");
        label.setFont(FONT.deriveFont(Font.BOLD, 32f));
        label.setForeground(FOREGROUND);

        JButton buttonNewGame = new CustomButton("New Game");
        buttonNewGame.setFont(buttonNewGame.getFont().deriveFont(20f));
        buttonNewGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buttonNewGame.setFocusable(false);
        buttonNewGame.addActionListener(evt -> model.getGame().reset());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(label, "West");
        panel.add(buttonNewGame, "East");
        return panel;
    }

    private Component createFooterPanel() {
        JLabel githubProject = new JLabel("Project: https://github.com/limagiran/2048");
        githubProject.putClientProperty("url", "https://github.com/limagiran/2048");

        JLabel githubClone = new JLabel("Based on: https://github.com/gabrielecirulli/2048");
        githubClone.putClientProperty("url", "https://github.com/gabrielecirulli/2048");

        for (JLabel label : new JLabel[]{githubProject, githubClone}) {
            label.setFont(RendererUtils.FONT.deriveFont(12f));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setForeground(FOREGROUND);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URL(label.getClientProperty("url").toString()).toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setOpaque(false);
        panel.add(githubProject, "First");
        panel.add(githubClone, "Last");
        return panel;
    }
}
