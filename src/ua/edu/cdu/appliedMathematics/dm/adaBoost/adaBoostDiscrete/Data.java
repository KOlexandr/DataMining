package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

public class Data {

    private Point x;
    private DataClass y;

    public Data(Point x, DataClass y) {
        this.x = x;
        this.y = y;
    }

    public Point getX() {
        return x;
    }

    public void setX(Point x) {
        this.x = x;
    }

    public DataClass getY() {
        return y;
    }

    public void setY(DataClass y) {
        this.y = y;
    }
}
