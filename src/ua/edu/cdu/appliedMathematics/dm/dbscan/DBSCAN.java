package ua.edu.cdu.appliedMathematics.dm.dbscan;

import java.util.HashSet;
import java.util.Set;

public class DBSCAN {

    private int minPt;
    private Set<Point> d;
    private Cluster noise;
    private Set<Cluster> clusters;

    public DBSCAN(int minPt, double eps, Set<Point> d) {
        this.d = d;
        this.minPt = minPt;
        this.noise = new Cluster(-1);
        this.clusters = new HashSet<>();

        for (Point point : d) {
            for (Point near : d) {
                if(!point.equals(near) && point.distance(near) <= eps){
                    point.getEpsNearPoints().add(near);
                }
            }
        }
        classify();
    }

    private void classify(){
        int  j = 0;
        Cluster cluster = new Cluster(j);
        clusters.add(cluster);
        for (Point point : d) {
            if (!point.isVisited()) {
                point.setVisited(true);
                if (point.getEpsNearPoints().size() < minPt) {
                    noise.getPoints().add(point);
                } else {
                    expandCluster(point, cluster);
                    cluster = new Cluster(++j);
                    clusters.add(cluster);
                }
            }
        }
    }

    private void expandCluster(final Point point, final Cluster cluster) {
        cluster.getPoints().add(point);
        Set<Point> tmp = new HashSet<>();
        for (Point epsNearPoint : point.getEpsNearPoints()) {
            if (!epsNearPoint.isVisited()) {
                epsNearPoint.setVisited(true);
                if (epsNearPoint.getEpsNearPoints().size() >= minPt) {
                    tmp.addAll(epsNearPoint.getEpsNearPoints());
                }
            }
            if (!belongToAnyCluster(epsNearPoint)) {
                cluster.getPoints().add(epsNearPoint);
            }
        }
        if(!tmp.isEmpty()){
            point.getEpsNearPoints().addAll(tmp);
            expandCluster(point, cluster);
        }
    }

    private boolean belongToAnyCluster(final Point point) {
        for (Cluster cluster : clusters) {
            if(cluster.getPoints().contains(point)){
                return true;
            }
        }
        return false;
    }

    public Cluster getNoise() {
        return noise;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }
}