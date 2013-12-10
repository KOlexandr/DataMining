package ua.edu.cdu.appliedMathematics.dm.dbscan;

import java.util.HashSet;
import java.util.Set;

public class DBSCAN {

    private int minPt;
    private Set<Point> d;
    private Cluster noise;
    private Set<Cluster> clusters;

    /**
     * initialize all fields and find all neighbor points of all points
     * @param minPt - minimum count of points for cluster
     * @param eps - maximum distance between points of cluster
     * @param d - set of initial points
     */
    public DBSCAN(int minPt, double eps, Set<Point> d) {
        this.d = d;
        this.minPt = minPt;
        this.noise = new Cluster(-1);
        this.clusters = new HashSet<>();

        //find all neighbor points for all points and set that to field in each point
        for (Point point : d) {
            for (Point near : d) {
                if(!point.equals(near) && point.distance(near) <= eps){
                    point.getEpsNearPoints().add(near);
                }
            }
        }
        classify();
    }

    /**
     * classify given data set
     */
    private void classify(){
        int  j = 0;
        Cluster cluster = new Cluster(j);
        clusters.add(cluster);
        //iterate by all points
        for (Point point : d) {
            //if not visited yet => visit
            if (!point.isVisited()) {
                point.setVisited(true);
                //if count of neighbor points < minPt then it's point is noise
                if (point.getEpsNearPoints().size() < minPt) {
                    noise.getPoints().add(point);
                } else {
                    expandCluster(point, cluster);
                    cluster = new Cluster(++j);
                    clusters.add(cluster);
                }
            }
        }
        clearNoiseCluster();
    }

    /**
     * process one point and all it neighbor points
     * @param point - given point for process
     * @param cluster - cluster for this point
     */
    private void expandCluster(final Point point, final Cluster cluster) {
        //add point to new cluster
        cluster.getPoints().add(point);
        //set for neighbor point, because we can't add items to list when we iterate on it
        Set<Point> tmp = new HashSet<>();
        //iterate by all neighbor point of input point
        for (Point epsNearPoint : point.getEpsNearPoints()) {
            //if not visited yet => visit
            if (!epsNearPoint.isVisited()) {
                epsNearPoint.setVisited(true);
                //if count of neighbor points >= minPt then add this points to tmp set
                if (epsNearPoint.getEpsNearPoints().size() >= minPt) {
                    tmp.addAll(epsNearPoint.getEpsNearPoints());
                }
            }
            //if current neighbor point is not a member of any cluster yet - add it to cluster
            if (!belongToAnyCluster(epsNearPoint)) {
                cluster.getPoints().add(epsNearPoint);
            }
        }
        //if set of new neighbor points is not empty =>
        //add it to set of neighbor points of point and recursive call this method again
        //old points from neighbor set already visited and we will not do anything with them again
        if(!tmp.isEmpty()){
            point.getEpsNearPoints().addAll(tmp);
            expandCluster(point, cluster);
        }
    }

    /**
     * verify if any of clusters contains given point
     * @param point - point for verification
     * @return true if some cluster has this point, false otherwise
     */
    private boolean belongToAnyCluster(final Point point) {
        for (Cluster cluster : clusters) {
            if(cluster.getPoints().contains(point)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return set of clusters with classified points
     */
    public Cluster getNoise() {
        return noise;
    }

    /**
     * @return noise cluster with points or empty set
     */
    public Set<Cluster> getClusters() {
        return clusters;
    }

    /**
     * if we have situation when we process boundary points at first
     * and they have neighbor points < minPt - they will classify as noise,
     * but this points may be densely achievable or tightly linked with
     * any point in some cluster
     * in this situation we must classify this point as point of that cluster
     *
     * as result at the end of classification we will remove all classified point
     * from noise cluster if they exists in another cluster
     */
    private void clearNoiseCluster(){
        for (Cluster cluster : clusters) {
            noise.getPoints().removeAll(cluster.getPoints());
        }
    }
}