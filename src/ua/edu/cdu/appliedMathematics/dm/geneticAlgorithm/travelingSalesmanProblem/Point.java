package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.travelingSalesmanProblem;

import java.util.Random;

/**
 * class represent point with x and y coordinates
 */
public class Point {

    private int x;
    private int y;

    private static final Random random = new Random();

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * count distance between this point and another
     * @param point - another point
     * @return Evclid distance
     */
    public double distance(final Point point){
        return (this.getX() - point.getX())*(this.getX() - point.getX()) + (this.getY() - point.getY())*(this.getY() - point.getY());
        //return Math.abs(this.getX() - point.getX());
    }

    /**
     * create Point with random x and y coordinates
     * @param maxX - max value for x coordinate
     * @param maxY -- max value for y coordinate
     * @return new object Point
     */
    public static Point getRandomPoint(final int maxX, final int maxY){
        return new Point(random.nextInt(maxX), random.nextInt(maxY));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        return x == point.x && y == point.y;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
