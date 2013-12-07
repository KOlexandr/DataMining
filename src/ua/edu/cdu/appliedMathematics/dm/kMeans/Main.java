package ua.edu.cdu.appliedMathematics.dm.kMeans;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]){
        final List<List<Point>> clusters = KMeans.kMeans(generatePoints(), 3);

        print(clusters);
/*
        final int num = KNN.kNN(clusters, new Point(59, 110), 3);
        System.out.println("Cluster number (kNN): " + num);
        printOneCluster(clusters.get(num));

        System.out.println("==================================================================");

        final int numModify = KNN.kNNModify(clusters, new Point(59, 110));
        System.out.println("Cluster number (kNNModify): " + numModify);
        printOneCluster(clusters.get(numModify));*/
    }

    /**
     * generate test points
     * @return list of points
     */
    private static List<Point> generatePoints() {
        final List<Point> points = new ArrayList<>();
        points.add(new Point(65, 220));
        points.add(new Point(73, 160));
        //points.add(new Point(59, 110));
        points.add(new Point(61, 120));
        points.add(new Point(75, 150));
        points.add(new Point(67, 240));
        points.add(new Point(68, 230));
        points.add(new Point(70, 220));
        points.add(new Point(62, 130));
        points.add(new Point(66, 210));
        points.add(new Point(77, 190));
        points.add(new Point(75, 180));
        points.add(new Point(74, 170));
        points.add(new Point(70, 210));
        points.add(new Point(61, 110));
        points.add(new Point(58, 100));
        points.add(new Point(66, 230));
        points.add(new Point(59, 120));
        points.add(new Point(68, 210));
        points.add(new Point(61, 130));
        return points;
    }

    /**
     * print all clusters
     * @param clusters list of clusters
     */
    public static void print(final List<List<Point>> clusters) {
        if(null != clusters){
            System.out.println("Clusters:");
            System.out.println("=======================");
            for(final List<Point> list : clusters){
                for(final Point point : list){
                    System.out.println(point);
                }
                System.out.println("=======================");
            }
        } else {
            System.out.println("No clusters!");
        }
    }

    /**
     * print one cluster
     * @param cluster list of points
     */
    public static void printOneCluster(final List<Point> cluster) {
        if(null != cluster){
            System.out.println("Cluster:");
            System.out.println("=======================");
            for(final Point point : cluster){
                System.out.println(point);
            }
            System.out.println("=======================");
        } else {
            System.out.println("No points!");
        }
    }
}