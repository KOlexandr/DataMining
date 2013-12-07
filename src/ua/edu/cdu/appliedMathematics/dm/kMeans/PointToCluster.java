package ua.edu.cdu.appliedMathematics.dm.kMeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represent point and distance from given point for kNN algorithm to some point from given clusters
 */
public class PointToCluster implements Comparable<PointToCluster> {

    private Point point;
    private int clusterNumber;
    private double distance;

    public PointToCluster(){}

    public PointToCluster(Point point, int clusterNumber, double distance) {
        this.point = point;
        this.clusterNumber = clusterNumber;
        this.distance = distance;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(PointToCluster o) {
        if(o.getClusterNumber() < this.getClusterNumber()){
            return 1;
        } else if(o.getClusterNumber() > this.getClusterNumber()){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "PointToCluster{" +
                "point=" + point +
                ", clusterNumber=" + clusterNumber +
                ", distance=" + distance +
                '}';
    }

    /**
     * transform list of clusters to list of PointToCluster objects
     * @param clusters list of clusters
     * @param point given point
     * @return list of PointToCluster
     */
    public static List<PointToCluster> transform(final List<List<Point>> clusters, final Point point){
        int clusterIndex = 0;
        final List<PointToCluster> ptc = new ArrayList<>();
        for(final List<Point> list: clusters){
            for(final Point pointTmp: list){
                ptc.add(new PointToCluster(pointTmp, clusterIndex, point.distance(pointTmp)));
            }
            clusterIndex++;
        }
        return ptc;
    }
}
