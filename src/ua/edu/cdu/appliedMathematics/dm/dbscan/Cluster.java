package ua.edu.cdu.appliedMathematics.dm.dbscan;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private int id;
    private Set<Point> points;

    public Cluster(int id) {
        this.id = id;
        this.points = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Cluster{id=" + id + ", points=" + points + "}";
    }
}