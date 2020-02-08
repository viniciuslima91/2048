package com.limagiran.tile2048;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Vinicius Silva
 */
public class View extends JFrame implements IPanel2048 {

    private final Game2048 game;

    public View() {
        super("2048 - by Lima Giran");
        game = new Game2048();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new Panel2048(View.this), "Center");
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_LEFT:
                        game.action(e.getKeyCode());
                        break;
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            new View().setVisible(true);
        });
    }

    @Override
    public Game2048 getGame() {
        return game;
    }
}
