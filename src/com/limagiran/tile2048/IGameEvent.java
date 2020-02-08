package com.limagiran.tile2048;

import java.awt.Point;

/**
 *
 * @author Vinicius Silva
 */
public interface IGameEvent {

    default void onNewGame() {
        //empty
    }

    default void onChange(Point discover, Move... moves) {
        //empty
    }

    default void onGameOver() {
        //empty
    }
}
