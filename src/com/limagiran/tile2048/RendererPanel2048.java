package com.limagiran.tile2048;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Vinicius Silva
 */
class RendererPanel2048 extends JComponent implements Constants, IGameEvent {

    private int[][] grid;
    private int[][] gridPrevious;
    private final IPanel2048 model;
    private final List<Move> moves = new ArrayList<>();
    private final Point discover = new Point();
    private final Object syncLock = new Object();
    private long actionDatetime;

    public RendererPanel2048(IPanel2048 model) {
        this.model = model;
        gridPrevious = model.getGame().getGridCopy();
        grid = gridPrevious;
        init();

    }

    private void init() {
        setDoubleBuffered(true);
        int gridLen = model.getGame().getGrid().length;
        int size = gridLen * TILE_PX + (gridLen + 1) * BORDER_PX;
        setSize(new Dimension(size, size));
        setPreferredSize(new Dimension(size, size));
        model.getGame().addListener(this);
    }

    @Override
    public void onChange(final Point discover, final Move... moves) {
        new Thread(() -> {
            synchronized (syncLock) {
                actionDatetime = System.currentTimeMillis();
                gridPrevious = grid;
                grid = model.getGame().getGridCopy();
                this.moves.clear();
                this.moves.addAll(Arrays.asList(moves));
                this.discover.setLocation(discover);
            }
        }).start();
    }

    @Override
    public void onGameOver() {
        //empty
    }

    @Override
    public void onNewGame() {
        new Thread(() -> {
            synchronized (syncLock) {
                actionDatetime = 0;
                grid = model.getGame().getGridCopy();
                gridPrevious = grid;
                this.moves.clear();
                this.discover.setLocation(-1, -1);
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RendererUtils.setupGraphics(g2);
        int w = getWidth();
        int h = getHeight();
        g2.setColor(TBORDER);
        g2.fillRoundRect(0, 0, w, h, 4, 4);
        long elapsedTimeAction = Math.abs(System.currentTimeMillis() - actionDatetime);
        float animationProgress = elapsedTimeAction >= ANIMATION ? 1 : elapsedTimeAction / (float) ANIMATION;
        for (int xIdx = 0; xIdx < grid.length; xIdx++) {
            for (int yIdx = 0; yIdx < grid[xIdx].length; yIdx++) {
                Point p = getLocationFromIndex(xIdx, yIdx);
                if (animationProgress >= 1) {
                    RendererUtils.getTileRendererSettings(grid[xIdx][yIdx], g2).paint(g2, p);
                    continue;
                }
                if (discover.x == xIdx && discover.y == yIdx) {
                    RendererUtils.getTileRendererSettings(0, g2).paint(g2, p);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animationProgress));
                    int diff = (int) (TILE_PX / 2 * (1f - animationProgress));
                    g2.clipRect(p.x + diff, p.y + diff, TILE_PX - diff - diff, TILE_PX - diff - diff);
                    RendererUtils.getTileRendererSettings(grid[xIdx][yIdx], g2).paint(g2, p);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    g2.setClip(null);
                    continue;
                }
                boolean flag = false;
                for (Move m : moves) {
                    if (m.to.x == xIdx && m.to.y == yIdx) {
                        RendererUtils.getTileRendererSettings(0, g2).paint(g2, p);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    RendererUtils.getTileRendererSettings(grid[xIdx][yIdx], g2).paint(g2, p);
                }
            }
        }
        if (animationProgress >= 1f) {
            paintGameOver(g2);
            return;
        }
        for (Move m : moves) {
            if (grid[m.to.x][m.to.y] <= 0) {
                continue;
            }
            Point coordFrom = getLocationFromIndex(m.from);
            Point coordTo = getLocationFromIndex(m.to);
            int xDif = (int) ((coordTo.x - coordFrom.x) * (1f - animationProgress));
            int yDif = (int) ((coordTo.y - coordFrom.y) * (1f - animationProgress));
            coordTo.x -= xDif;
            coordTo.y -= yDif;
            RendererUtils.getTileRendererSettings(m.valFrom, g2).paint(g2, coordTo);
        }
        paintGameOver(g2);
    }

    private void paintGameOver(Graphics2D g2) {
        if (!model.getGame().isGameOver()) {
            return;
        }
        long elapsedTimeAction = Math.abs(System.currentTimeMillis() - actionDatetime);
        float animationProgress = elapsedTimeAction >= ANIMATION ? 1 : elapsedTimeAction / (float) ANIMATION;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f * animationProgress));
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animationProgress));
        g2.setColor(Constants.FOREGROUND);
        g2.setFont(RendererUtils.FONT.deriveFont(64f));
        FontMetrics fm = g2.getFontMetrics(RendererUtils.FONT.deriveFont(64f));
        String str = "Game over";
        int xStr = (getWidth() - fm.stringWidth(str)) / 2;
        int yStr = fm.getAscent() + fm.getLeading() + ((getHeight() - fm.getHeight()) / 2);
        g2.drawString(str, xStr, yStr);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public Point getLocationFromIndex(int x, int y) {
        return new Point(
                x * (BORDER_PX + TILE_PX) + BORDER_PX,
                y * (BORDER_PX + TILE_PX) + BORDER_PX
        );
    }

    public Point getLocationFromIndex(Point idx) {
        return getLocationFromIndex(idx.x, idx.y);
    }
}
