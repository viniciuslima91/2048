package com.limagiran.tile2048;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author Vinicius Silva
 */
public class Game2048 implements Constants {

    private final int[][] grid;
    private final List<IGameEvent> listeners;
    private final Object listenersLock = new Object();
    private boolean gameOver = false;

    public Game2048() {
        this.grid = new int[GRID_WEIGHT][GRID_WEIGHT];
        listeners = new ArrayList<>();
        reset();
    }

    public final void reset() {
        gameOver = false;
        for (int[] col : grid) {
            Arrays.fill(col, 0);
        }
        discover();
        discover();
        SwingUtilities.invokeLater(() -> {
            synchronized (listenersLock) {
                for (IGameEvent evt : listeners) {
                    evt.onNewGame();
                }
            }
        });
    }

    public boolean isAnyAvailableTile() {
        for (int[] col : grid) {
            for (int n : col) {
                if (n == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * setup empty tile to number 2 or 4
     *
     * @return
     */
    public Point discover() {
        if (!isAnyAvailableTile()) {
            return null;
        }
        int x, y;
        do {
            x = Utils.RANDOM.nextInt(grid.length);
            y = Utils.RANDOM.nextInt(grid[x].length);
        } while (grid[x][y] > 0);
        grid[x][y] = Utils.RANDOM.nextInt(RANDOM_FOUR_OCCURRENCE) > 0 ? 2 : 4;
        return new Point(x, y);
    }

    /**
     * {@link java.awt.event.KeyEvent#VK_UP}
     * {@link java.awt.event.KeyEvent#VK_RIGHT}
     * {@link java.awt.event.KeyEvent#VK_DOWN}
     * {@link java.awt.event.KeyEvent#VK_LEFT}
     *
     * @param keyCode KeyEvent code
     * @return {@code true} continue. {@code false} game over.
     */
    public boolean action(int keyCode) {
        List<Move> moves = fireMove(keyCode);
        if (moves.isEmpty()) {
            return false;
        }
        Point discover = discover();
        if (discover == null) {
            return false;
        }
        SwingUtilities.invokeLater(() -> {
            synchronized (listenersLock) {
                for (IGameEvent evt : listeners) {
                    evt.onChange(discover, moves.toArray(new Move[0]));
                }
            }
        });
        gameOver = checkGameOver();
        if (gameOver) {
            SwingUtilities.invokeLater(() -> {
                synchronized (listenersLock) {
                    for (IGameEvent evt : listeners) {
                        evt.onGameOver();
                    }
                }
            });
            return false;
        }

        return true;
    }

    /**
     * {@link java.awt.event.KeyEvent#VK_UP}
     * {@link java.awt.event.KeyEvent#VK_RIGHT}
     * {@link java.awt.event.KeyEvent#VK_DOWN}
     * {@link java.awt.event.KeyEvent#VK_LEFT}
     *
     * @return moves
     */
    private List<Move> fireMove(int keyCode) {
        final List<Move> moves = new ArrayList<>();
        boolean horizontal = true;
        int offset = 0;
        int max = GRID_WEIGHT - 1;
        final Point direction = new Point();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                direction.setLocation(0, -1);
                horizontal = false;
                break;
            case KeyEvent.VK_RIGHT:
                direction.setLocation(1, 0);
                offset = -(GRID_WEIGHT - 1);
                max = 0;
                break;
            case KeyEvent.VK_DOWN:
                direction.setLocation(0, 1);
                horizontal = false;
                offset = -(GRID_WEIGHT - 1);
                max = 0;
                break;
            case KeyEvent.VK_LEFT:
                direction.setLocation(-1, 0);
                break;
            default:
                return moves;
        }
        for (int i = offset; i <= max; i++) {
            for (int j = 0; j < GRID_WEIGHT; j++) {
                Point to = new Point(
                        horizontal ? Math.abs(i) : j,
                        horizontal ? j : Math.abs(i)
                );
                final List<Point> previous = new ArrayList<>();
                Point p = new Point(to);
                while (true) {
                    p.x += -direction.x;
                    p.y += -direction.y;
                    if (p.x < 0 || p.y < 0 || p.x >= grid.length || p.y >= grid.length) {
                        break;
                    }
                    if (grid[p.x][p.y] > 0) {
                        previous.add(new Point(p));
                    }
                }
                if (previous.isEmpty()) {
                    continue;
                }
                if (getValue(to) == 0) {
                    if (previous.size() == 1) {
                        Point from = previous.get(0);
                        moves.add(createMove(from, to));
                        setValue(to, getValue(from));
                        setValue(from, 0);
                    } else {
                        while (previous.size() > 2) {
                            previous.remove(previous.size() - 1);
                        }
                        Point p1 = previous.get(0);
                        Point p2 = previous.get(1);
                        if (getValue(p1) == getValue(p2)) {
                            moves.add(createMove(p1, to));
                            moves.add(createMove(p2, to));
                            setValue(to, getValue(p1) * 2);
                            setValue(p1, 0);
                            setValue(p2, 0);
                        } else {
                            moves.add(createMove(p1, to));
                            setValue(to, getValue(p1));
                            setValue(p1, 0);
                        }
                    }
                } else {
                    while (previous.size() > 1) {
                        previous.remove(previous.size() - 1);
                    }
                    Point from = previous.get(0);
                    if (getValue(from) == getValue(to)) {
                        moves.add(createMove(to, to));
                        moves.add(createMove(from, to));
                        setValue(to, getValue(from) * 2);
                        setValue(from, 0);
                    }
                }
            }
        }
        return moves;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getGridCopy() {
        int[][] copy = new int[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                copy[x][y] = grid[x][y];
            }
        }
        return copy;
    }

    public int getValue(Point p) {
        return grid[p.x][p.y];
    }

    public void setValue(Point p, int value) {
        grid[p.x][p.y] = value;
    }

    public void addListener(IGameEvent evt) {
        synchronized (listenersLock) {
            listeners.add(evt);
        }
    }

    public void removeListener(IGameEvent evt) {
        synchronized (listenersLock) {
            listeners.remove(evt);
        }
    }

    public Move createMove(Point from, Point to) {
        return new Move(from, getValue(from), to, getValue(to));
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean checkGameOver() {
        if (isAnyAvailableTile()) {
            return false;
        }
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                AroundValues around = getAroundValues(x, y);
                if (around.contains(grid[x][y])) {
                    return false;
                }
            }
        }
        return true;
    }

    public AroundValues getAroundValues(int x, int y) {
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        int x2, y2;
        AroundValues around = new AroundValues();
        for (int i = 0; i < directions.length; i++) {
            Integer val = null;
            x2 = x + directions[i][0];
            y2 = y + directions[i][1];
            if (x2 >= 0 && x2 < grid.length && y2 >= 0 && y2 < grid.length) {
                val = grid[x2][y2];
            }
            switch (i) {
                case 0:
                    around.top = val;
                    break;
                case 1:
                    around.right = val;
                    break;
                case 2:
                    around.bottom = val;
                    break;
                case 3:
                    around.left = val;
                    break;
            }
        }
        return around;
    }
}
