package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

/**
 * class represent point with x1 and x2 coordinates
 */
public class Point {

    private int x1;
    private int x2;

    public Point(int x1, int x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
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

        return x1 == point.x1 && x2 == point.x2;
    }

    @Override
    public String toString() {
        return "{" + "x1=" + x1 + ", x2=" + x2 + '}';
    }
}