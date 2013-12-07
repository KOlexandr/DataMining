package ua.edu.cdu.appliedMathematics.dm.kMeans;

import java.util.*;

public class KNN {

    /**
     * algorithm kNN (k-nearest neighbor)
     * @param clusters list of clusters
     * @param point test point
     * @param k
     * @return index of cluster in clusters
     */
    public static int kNN(final List<List<Point>> clusters, final Point point, final int k){
        //transform clusters to list of PointToCluster
        final List<PointToCluster> ptc = PointToCluster.transform(clusters, point);
        //copy original ptc list
        final List<PointToCluster> tmp = copyList(ptc);

        //get array with k nearest points to test point (with smallest distance)
        final PointToCluster[] closest = getKClosest(k, tmp);
        System.out.println("Closest " + k + " points:");
        print(closest);
        //return number of cluster which has most points in closest array
        return getClusterNumber(closest);
    }

    /**
     * copy some list
     * @param list
     * @param <T>
     * @return copy of input list
     */
    private static <T> List<T> copyList(final List<T> list) {
        final List<T> copy = new ArrayList<>();
        for(final T item: list){
            copy.add(item);
        }
        return copy;
    }

    /**
     * modify algorithm kNN
     * @param clusters list of clusters
     * @param point test point
     * @return index of cluster in clusters
     */
    public static int kNNModify(final List<List<Point>> clusters, final Point point){
        final Point[] centroids = new Point[clusters.size()];
        int j = 0;
        //find centroids of given clusters
        for(final List<Point> list: clusters){
            double xSum = 0;
            double ySum = 0;
            for(final Point p: list){
                xSum += p.getX();
                ySum += p.getY();
            }
            centroids[j++] = new Point(xSum/list.size(), ySum/list.size());
        }
        int clusterNumber = 0;
        double distance = Double.MAX_VALUE;
        //find smallest distance from test point to centroid
        for(int i = 0; i < centroids.length; i++){
            if(distance < centroids[i].distance(point)){
                clusterNumber = i;
                distance = centroids[i].distance(point);
            }
        }
        //return index of cluster
        return clusterNumber;
    }

    /**
     * find k closest point to test point
     * @param k
     * @param ptc list of PointToCluster
     * @return array of closest points
     */
    private static PointToCluster[] getKClosest(final int k, final List<PointToCluster> ptc){
        final PointToCluster[] closest = new PointToCluster[k];
        for(int i = 0; i < k; i++){
            closest[i] = getMinValueFromPTCList(ptc);
            ptc.remove(closest[i]);
        }
        return closest;
    }

    /**
     * find number of cluster which has most points in closest array
     * @param closest
     * @return index of cluster
     */
    private static int getClusterNumber(final PointToCluster[] closest) {
        final Integer indexes[] = new Integer[closest.length];
        for(int i = 0; i < closest.length; i++){
            indexes[i] = closest[i].getClusterNumber();
        }
        final Map<Integer, Integer> map = new HashMap<>();
        for (final Integer index : indexes) {
            if (map.containsKey(index)) {
                map.put(index, map.get(index) + 1);
            } else {
                map.put(index, 1);
            }
        }
        return getMaxValueInMap(map);
    }

    /**
     * find max value in map
     * @param map
     * @return index of cluster which has most points in closest array
     */
    private static int getMaxValueInMap(final Map<Integer, Integer> map) {
        Integer maxValue = 0;
        Integer key = 0;
        for (final Map.Entry<Integer, Integer> entry: map.entrySet()){
            if(maxValue < entry.getValue()){
                maxValue = entry.getValue();
                key = entry.getKey();
            }
        }
        return key;
    }

    /**
     * find min distance from test point to any point in PointToCluster list
     * @param ptc
     * @return PointToCluster
     */
    private static PointToCluster getMinValueFromPTCList(final List<PointToCluster> ptc) {
        PointToCluster min = ptc.get(0);
        for(int i = 1; i < ptc.size(); i++){
            if(min.getDistance() > ptc.get(i).getDistance()){
                min = ptc.get(i);
            }
        }
        return min;
    }

    /**
     * print PointToCluster
     * @param ptc
     */
    private static void print(final PointToCluster[] ptc) {
        System.out.println("=======================");
        for(final PointToCluster point : ptc){
            System.out.println(point);
        }
        System.out.println("=======================");
    }

    /**
     * print one cluster
     * @param cluster
     */
    private static void print(final List<Point> cluster) {
        System.out.println("=======================");
        for(final Point point : cluster){
            System.out.println(point);
        }
        System.out.println("=======================");
    }
}