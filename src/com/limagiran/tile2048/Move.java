package com.limagiran.tile2048;

import java.awt.Point;

/**
 *
 * @author Vinicius Silva
 */
public class Move {

    public final Point from;
    public final int valFrom;
    public final Point to;
    public final int valTo;

    public Move(Point from, int valFrom, Point to, int valTo) {
        this.from = new Point(from);
        this.valFrom = valFrom;
        this.to = new Point(to);
        this.valTo = valTo;
    }
}
