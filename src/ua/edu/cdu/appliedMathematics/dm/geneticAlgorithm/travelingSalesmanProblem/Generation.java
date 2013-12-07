package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.travelingSalesmanProblem;

import java.util.Arrays;

/**
 * represent one generation
 * with sum of distances between all points (coordinates of cities)
 * and has coordinates of cities
 */
public class Generation {

    private Double distance;
    private Point[] chromosomes;

    public Generation(Double distance, Point[] chromosomes) {
        this.distance = distance;
        this.chromosomes = chromosomes;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Point[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Point[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * verify distances in 2 generation and return generation which has smaller distance
     * @param g1 - first generation
     * @param g2 - second generation
     * @return generation with smaller distance between all cities
     */
    public static Generation betterGeneration(final Generation g1, final Generation g2){
        return g1.getDistance() < g2.getDistance() ? g1 : g2;
    }

    @Override
    public String toString() {
        return "Generation {distance = " + distance + ", chromosomes = " + Arrays.toString(chromosomes) + '}';
    }
}
