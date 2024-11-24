package com.jake;

/**
 * A representation of one Dot, which has a single color and belongs to at least
 *  one Circle.
 */
public class Dot {

    /**
     * The color value of this Dot. Either 'R' for Red, 'Y' for Yellow, or 'G' for Green.
     */
    private char color;

    /**
     * Constructor for a Dot.
     * @param color This Dot's initial color.
     */
    public Dot(char color) {
        this.color = color;
    }

    public char getColor() {
        return color;
    }
    public void setColor(char color) {
        this.color = color;
    }
}
