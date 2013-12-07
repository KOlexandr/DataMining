package ua.edu.cdu.appliedMathematics.dm.kMeans;

/**
 * Class represent simple point with x and y coordinates
 */
public class Point {

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distance(final Point point){
        return Math.sqrt((this.x - point.x)*(this.x - point.x) + (this.y - point.y)*(this.y - point.y));
    }

    @Override
    public String toString() {
        return "Point{ " +
                "x=" + x +
                ", y=" + y +
                " }";
    }
}
