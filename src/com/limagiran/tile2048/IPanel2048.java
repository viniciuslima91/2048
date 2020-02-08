package com.limagiran.tile2048;

/**
 *
 * @author Vinicius Silva
 */
public interface IPanel2048 {

    Game2048 getGame();
    
    default boolean isRendererPanelAlive() {
        return true;
    }
}
