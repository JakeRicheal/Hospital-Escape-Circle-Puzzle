package com.jake;

public class Circle {

    /**
     * A unique descriptor for this Circle.
     */
    private final String id;

    /**
     * The array of Dots that make up this Circle. Index 0 represents the top-left corner,
     *  and Dots go clockwise.
     */
    private final Dot[] dots;

    /**
     * Constructor for a Circle of eight Dots.
     * @param dots Configuration of which Dot objects comprise this Circle.
     */
    public Circle (String id, Dot[] dots) {
        this.id = id;
        this.dots = dots;
    }

    public String getId() {
        return id;
    }

    /**
     * Rotate the Circle right (clockwise).
     */
    public void rotateRight() {
        char temp = dots[dots.length - 1].getColor();
        for (int i = dots.length - 1; i > 0; i--) {
            dots[i].setColor(dots[i - 1].getColor());
        }
        dots[0].setColor(temp);
    }

    /**
     * Rotate the Circle left (counter-clockwise).
     */
    public void rotateLeft() {
        char temp = dots[0].getColor();
        for (int i = 0; i < dots.length - 1; i++) {
            dots[i].setColor(dots[i + 1].getColor());
        }
        dots[dots.length - 1].setColor(temp);
    }

    /**
     * Print out this Circle's contents as a String of Dot colors.
     */
    public String toString() {
        StringBuilder colorString = new StringBuilder();
        for (Dot dot : dots) {
            colorString.append(dot.getColor());
        }
        return colorString.toString();
    }
}
