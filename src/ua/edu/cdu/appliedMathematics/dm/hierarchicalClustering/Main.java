package ua.edu.cdu.appliedMathematics.dm.hierarchicalClustering;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        final List<Point> points = readPointsFromFile("src/ua/edu/cdu/appliedMathematics/dm/hierarchicalClustering/test.txt");

        /**
         * this are tests for 3 different type of counting similarity between clusters
         * for each type we must use specific parameters of eps
         * classification with eps which i use take good results, its same as in DBSCAN,
         * but classification with type GROUP_AVERAGE classify one noise point as part of one of clusters
         * all type is usable, and all has some strengths and limitations
         * one type of counting similarity better for one data and worse for another
         * as result i can say that type GROUP_AVERAGE worse for test data then MIN and MAX
         */

        final HierarchicalClustering hc = new HierarchicalClustering(points, 6, 3, InterClassSimilarityType.MIN);
        //final HierarchicalClustering hc = new HierarchicalClustering(points, 20, 3, InterClassSimilarityType.MAX);
        //final HierarchicalClustering hc = new HierarchicalClustering(points, 6, 3, InterClassSimilarityType.GROUP_AVERAGE);

        printClusters(hc.getResultClusters());
        System.out.println("Noise: " + hc.getNoiseCluster());
    }

    private static void printClusters(final List<Cluster> clusters){
        System.out.println("Clusters:");
        for (Cluster cluster : clusters) {
            if(!cluster.getPoints().isEmpty()){
                System.out.println(cluster);
            }
        }
        System.out.println();
    }

    /**
     * read coordinate of points from file and create set of points
     * @param pathToFile - path to file with data
     * @return - set of points for classification
     * @throws java.io.IOException
     */
    private static List<Point> readPointsFromFile(final String pathToFile) throws IOException {
        final List<Point> points = new ArrayList<>();
        final List<String> strings = Files.readAllLines(Paths.get(pathToFile), Charset.defaultCharset());
        for (String string : strings) {
            final String[] split = string.split(",");
            final double[] coordinates = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                coordinates[i] = Double.parseDouble(split[i]);
            }
            points.add(new Point(coordinates, coordinates.length));
        }
        return points;
    }
}
