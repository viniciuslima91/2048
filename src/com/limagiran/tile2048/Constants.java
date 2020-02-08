package com.limagiran.tile2048;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Vinicius Silva
 */
public interface Constants {

    /**
     * Fonte padrão
     */
    public static final Font FONT = new Font("Arial", Font.PLAIN, 8);
    /**
     * quantidade de linhas e colunas
     */
    public static final int GRID_WEIGHT = 4;
    /**
     * tamanho do quadrado em pixels
     */
    public static final int TILE_PX = 96;
    /**
     * largura da borda em px
     */
    public static final int BORDER_PX = 12;
    /**
     * chances de aparecer o número 4 em vez do número 2<br>
     * 1 a cada RANDOM_FOUR_OCCURRENCE vezes
     */
    public static final int RANDOM_FOUR_OCCURRENCE = 5;
    /**
     * duração da animação em milésimos de segundo
     */
    public static final long ANIMATION = 100;

    public static final Color BACKGROUND = new Color(250, 248, 239);
    public static final Color FOREGROUND = new Color(119, 111, 102);
    public static final Color FOREGROUND_LIGHT = new Color(249, 246, 242);
    public static final Color TBORDER = new Color(187, 173, 160);

}
