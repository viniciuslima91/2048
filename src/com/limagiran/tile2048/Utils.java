package com.limagiran.tile2048;

import java.util.Random;
import javax.swing.UIManager;

/**
 *
 * @author Vinicius Silva
 */
public class Utils {

    public static final Random RANDOM = new Random();

    public static void applySystemLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //ignore
        }
    }

    public static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            //ignore
        }
    }
}
