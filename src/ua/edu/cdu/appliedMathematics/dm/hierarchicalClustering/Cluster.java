package ua.edu.cdu.appliedMathematics.dm.hierarchicalClustering;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private int id;
    private Set<Point> points;
    private boolean processed;

    public Cluster(int id) {
        this.id = id;
        this.processed = false;
        this.points = new HashSet<>();
    }

    public Cluster() {
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "Cluster{id=" + id + ", points=" + points + "}";
    }
}