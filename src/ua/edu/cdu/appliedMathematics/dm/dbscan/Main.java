package ua.edu.cdu.appliedMathematics.dm.dbscan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        final Set<Point> points = new HashSet<>(Arrays.asList(new Point(new double[]{1.28}, 1), new Point(new double[]{1.30}, 1), new Point(new double[]{1.32}, 1),
                                                  new Point(new double[]{1.36}, 1), new Point(new double[]{1.37}, 1), new Point(new double[]{1.39}, 1),
                                                  new Point(new double[]{1.43}, 1)));
        final DBSCAN dbscan = new DBSCAN(2, 0.04, points);
        printClusters(dbscan.getClusters());
        System.out.println("Noise:\n" + dbscan.getNoise());
    }

    private static void printClusters(final Set<Cluster> clusters){
        System.out.println("Clusters:");
        for (Cluster cluster : clusters) {
            if(!cluster.getPoints().isEmpty()){
                System.out.println(cluster);
            }
        }
        System.out.println();
    }
}
