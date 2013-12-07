package ua.edu.cdu.appliedMathematics.dm.kMeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans {

    public static final double EPS = 0.0001;

    /**
     * algorithm kMeans
     * @param points all given points
     * @param numberClusters number of clusters
     * @return list of clusters (cluster is list of points)
     */
    public static List<List<Point>> kMeans(final List<Point> points, int numberClusters){
        if(null == points){
            return null;
        } else if(points.size() < numberClusters){  //if number of clusters > count of all given points
            numberClusters = points.size();
        }
        //select numberClusters different point (centroids) from input list
        Point[] clusterCentroidsBefore = getRandomCentroids(numberClusters, points);
        //copy centroids
        Point[] clusterCentroidsAfter = Arrays.copyOf(clusterCentroidsBefore, numberClusters);
        //make empty with empty clusters
        final List<List<Point>> clusters = makeEmptyClusterList(numberClusters);
        do {
            //clear every cluster in list
            clearClusters(clusters);
            //copy centroids from prew step
            clusterCentroidsBefore = Arrays.copyOf(clusterCentroidsAfter, numberClusters);
            //get number cluster with closest centroid for all points and add point to this cluster
            for(final Point point: points){
                clusters.get(getClusterNumber(point, clusterCentroidsBefore)).add(point);
            }
            //find new centroids with coordinates as median of coordinates of points in cluster
            clusterCentroidsAfter = findNewCentroids(clusters, numberClusters);
        //verify if distance between centroids before and after cycle is < then EPS
        } while(isDifferent(clusterCentroidsBefore, clusterCentroidsAfter));
        //return list of clusters
        return clusters;
    }

    /**
     * algorithm kMedoid
     * @param points all given points
     * @param numberClusters number of clusters
     * @return list of clusters (cluster is list of points)
     */
    public static List<List<Point>> kMedoids(final List<Point> points, int numberClusters){
        if(null == points){
            return null;
        } else if(points.size() < numberClusters){
            numberClusters = points.size();
        }
        Point[] clusterCentroidsBefore = getRandomCentroids(numberClusters, points);
        Point[] clusterCentroidsAfter = Arrays.copyOf(clusterCentroidsBefore, numberClusters);
        final List<List<Point>> clusters = makeEmptyClusterList(numberClusters);
        do {
            clearClusters(clusters);
            clusterCentroidsBefore = Arrays.copyOf(clusterCentroidsAfter, numberClusters);
            for(final Point point: points){
                clusters.get(getClusterNumber(point, clusterCentroidsBefore)).add(point);
            }
            clusterCentroidsAfter = findNewMedoids(clusters, numberClusters);
        } while(isDifferent(clusterCentroidsBefore, clusterCentroidsAfter));
        return clusters;
    }

    /**
     * find new medoids for kMedoid algorithm
     * @param clusters list of clusters
     * @param numberClusters number of clusters
     * @return medoids (centroids)
     */
    private static Point[] findNewMedoids(final List<List<Point>> clusters, final int numberClusters) {
        final Point[] medoids = new Point[numberClusters];
        int i = 0;
        for(final List<Point> list: clusters){
            Point medoid = null;
            double minSumDistance = Double.MAX_VALUE;
            for(final Point posibleMedoid: list){
                double sumDistance = 0;
                for(final Point point: list){
                    sumDistance += point.distance(posibleMedoid);
                }
                if(minSumDistance > sumDistance){
                    minSumDistance = sumDistance;
                    medoid = posibleMedoid;
                }
            }
            medoids[i++] = medoid;
        }
        return medoids;
    }

    /**
     * verify if any distance between centroids before and after cycle is < EPS
     * @param clusterCentroidsBefore
     * @param clusterCentroidsAfter
     * @return
     */
    private static boolean isDifferent(final Point[] clusterCentroidsBefore, final Point[] clusterCentroidsAfter) {
        final int length = clusterCentroidsBefore.length;
        int count = 0;
        for(int i = 0; i < length; i++){
            final Point pointBefore = clusterCentroidsBefore[i];
            int j = 0;
            while(j < length && !(Math.abs(pointBefore.getX() - clusterCentroidsAfter[j].getX()) < EPS &&
                    Math.abs(pointBefore.getY() - clusterCentroidsAfter[j].getY()) < EPS)){
                j++;
            }
            if(j != i){
                count++;
            }
        }
        return count == length;
    }

    /**
     * clear every clusters
     * @param clusters list of clusters
     */
    private static void clearClusters(final List<List<Point>> clusters) {
        for(final List<Point> list : clusters){
            list.clear();
        }
    }

    /**
     * find new centroids for kMeans algorithm
     * @param clusters list of clusters
     * @param numberClusters number of clusters
     * @return centroids
     */
    private static Point[] findNewCentroids(final List<List<Point>> clusters, final int numberClusters) {
        final Point[] centroids = new Point[numberClusters];
        int i = 0;
        for(final List<Point> list: clusters){
            double xSum = 0;
            double ySum = 0;
            for(final Point point: list){
                xSum += point.getX();
                ySum += point.getY();
            }
            centroids[i++] = new Point(xSum/list.size(), ySum/list.size());
        }
        return centroids;
    }

    /**
     * create list with empty clusters
     * @param numberClusters number of clusters
     * @return list of clusters
     */
    private static List<List<Point>> makeEmptyClusterList(final int numberClusters) {
        final List<List<Point>> clusters = new ArrayList<>();
        for(int i = 0; i < numberClusters; i++){
            clusters.add(new ArrayList<Point>());
        }
        return clusters;
    }

    /**
     * get number of cluster which centroid is closest to this point
     * @param point
     * @param clusterCenters array of centroids
     * @return number (index in list of clusters)
     */
    private static int getClusterNumber(final Point point, final Point[] clusterCenters) {
        double minDistance = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < clusterCenters.length; i++) {
            double distance = point.distance(clusterCenters[i]);
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }
        return index;
    }

    /**
     * select numberClusters different point (centroids) from input list
     * @param numberClusters
     * @param data input points
     * @return array of centroids
     */
    private static Point[] getRandomCentroids(final int numberClusters, final List<Point> data) {
        final Point[] points = new Point[numberClusters];
        final int[] pointNumbers = makeRandomArrayWithUniqueValues(numberClusters, data.size());
        for(int i = 0; i < numberClusters; i++){
            points[i] = data.get(pointNumbers[i]);
        }
        return points;
    }

    /**
     * select only unique indexes of points
     * @param numberClusters
     * @param dataSize size of list with input points
     * @return array of different int values from 0 to dataSize-1
     */
    private static int[] makeRandomArrayWithUniqueValues(final int numberClusters, final int dataSize) {
        final int[] pointsNumbers = new int[numberClusters];
        final Random rand = new Random();
        for(int i = 0; i < numberClusters; i++){
            int tmp, j;
            do {
                tmp = rand.nextInt(dataSize);
                for(j = 0; j < i; j++){
                    if(tmp == pointsNumbers[j]){
                        break;
                    }
                }
            } while(j < i);
            pointsNumbers[i] = tmp;
        }
        return pointsNumbers;
    }
}