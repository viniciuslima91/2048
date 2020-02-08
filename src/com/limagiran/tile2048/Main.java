package com.limagiran.tile2048;

/**
 *
 * @author Vinicius Silva
 */
public class Main {

    public static void main(String[] args) {
        setup();
        View.open();
    }

    private static void setup() {
        Utils.applySystemLAF();
    }

}
