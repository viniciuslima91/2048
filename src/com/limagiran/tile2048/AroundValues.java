package com.limagiran.tile2048;

/**
 *
 * @author Vinicius Silva
 */
public class AroundValues {

    public Integer top;
    public Integer right;
    public Integer bottom;
    public Integer left;

    public AroundValues(Integer top, Integer right, Integer down, Integer left) {
        this.top = top;
        this.right = right;
        this.bottom = down;
        this.left = left;
    }

    public AroundValues() {
    }

    public boolean contains(int val) {
        return (top != null && top == val)
                || (right != null && right == val)
                || (bottom != null && bottom == val)
                || (left != null && left == val);
    }
}
