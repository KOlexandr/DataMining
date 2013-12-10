package ua.edu.cdu.appliedMathematics.dm.dbscan;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        /*final Set<Point> points = new HashSet<>(Arrays.asList(new Point(new double[]{1.28}, 1), new Point(new double[]{1.30}, 1), new Point(new double[]{1.32}, 1),
                                                  new Point(new double[]{1.36}, 1), new Point(new double[]{1.37}, 1), new Point(new double[]{1.39}, 1),
                                                  new Point(new double[]{1.43}, 1)));*/
        final Set<Point> points = readPointsFromFile("d:/test.txt");
        final DBSCAN dbscan = new DBSCAN(4, 6, points);
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

    /**
     * read coordinate of points from file and create set of points
     * @param pathToFile - path to file with data
     * @return - set of points for DBSCAN
     * @throws IOException
     */
    private static Set<Point> readPointsFromFile(final String pathToFile) throws IOException {
        final Set<Point> points = new HashSet<>();
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
