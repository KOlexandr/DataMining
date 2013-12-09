package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.differentialEvolution;

import java.util.Arrays;
import java.util.Random;

public class Point {
    private int dimension;
    private double[] coordinates;

    private static final Random random = new Random();

    public Point(double[] coordinates, int dimension) {
        this.dimension = dimension;
        this.coordinates = coordinates;
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
}
