package ua.edu.cdu.appliedMathematics.dm.agglomerativeMethod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Point {
    private int dimension;
    private boolean visited;
    private double[] coordinates;
    private Set<Point> epsNearPoints;

    private static final Random random = new Random();

    public Point(double[] coordinates, int dimension) {
        this.visited = false;
        this.dimension = dimension;
        this.coordinates = coordinates;
        this.epsNearPoints = new HashSet<>();
    }

    /**
     * create Point with random x and y coordinates
     * @param maxBoundaries - max values for all coordinates
     * @param minBoundaries -- max values for all coordinates
     * @param dimension - dimension of space (count of coordinates)
     * @return new object Point
     */
    public static Point getRandomPoint(final double[] maxBoundaries, final double[] minBoundaries, final int dimension){
        if(maxBoundaries.length != dimension || minBoundaries.length != dimension){
            throw new RuntimeException("Wrong data.");
        }
        final double[] coordinates = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            coordinates[i] = minBoundaries[i] + (maxBoundaries[i]-minBoundaries[i]) * random.nextDouble();
        }
        return new Point(coordinates, dimension);
    }

    @Override
    public String toString() {
        return "Point{coordinates=" + Arrays.toString(coordinates) + ", dimension=" + dimension + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return dimension == point.dimension && Arrays.equals(coordinates, point.coordinates);

    }

    /**
     * count distance between this point and another
     * @param point - another point
     * @return Euclidean distance
     */
    public double distance(final Point point){
        double dist = 0;
        for (int i = 0; i < coordinates.length; i++) {
            dist += (coordinates[i] - point.getCoordinates()[i])*(coordinates[i] - point.getCoordinates()[i]);
        }
        return Math.sqrt(dist);
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Set<Point> getEpsNearPoints() {
        return epsNearPoints;
    }

    public void setEpsNearPoints(Set<Point> epsNearPoints) {
        this.epsNearPoints = epsNearPoints;
    }
}
