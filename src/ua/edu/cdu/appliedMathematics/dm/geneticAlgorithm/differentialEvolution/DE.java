package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.differentialEvolution;

import java.util.Random;

public class DE {

    private static final Random random = new Random();

    //space-search dimensionality
    private int n;
    //F є [0,2]
    private double F;
    //CR є [0,1]
    private double CR;
    //size of one population
    private int m;

    private Point[] x;
    private double[] minBoundaries;
    private double[] maxBoundaries;
    private double[] fitnesses;

    /**
     * constructor
     * @param n - dimension of space of task
     * @param m - points in one generation
     * @param F - the differential weight
     * @param CR - the crossover probability
     * @param minBoundaries - array of min boundaries of points for all points
     * @param maxBoundaries - array of max boundaries of points for all points
     */
    public DE(int n, int m, double F, double CR, double[] minBoundaries, double[] maxBoundaries) {
        this.n = n;
        this.m = m;
        this.F = F;
        this.CR = CR;
        this.x = new Point[m];
        this.fitnesses = new double[m];
        this.minBoundaries = minBoundaries;
        this.maxBoundaries = maxBoundaries;
        this.init();
    }

    /**
     * initialize array by random points and count fitness for all
     */
    private void init(){
        fillByUniquePoints(0, m, x);
        for (int i = 0; i < m; i++) {
            fitnesses[i] = fitness(x[i]);
        }
    }

    /**
     * fill array by new random points
     * @param start - start index in subArray
     * @param end - end index of subArray
     * @param points array with exist points
     */
    private void fillByUniquePoints(final int start, final int end, final Point[] points) {
        for (int i = start; i < end; i++) {
            Point newPoint = generateNewRandomPoint();
            while(isExistPoint(points, newPoint)){
                newPoint = generateNewRandomPoint();
            }
            points[i] = newPoint;
        }
    }

    /**
     * realizes Differential Evolution algorithm for one external step
     */
    public void solve(){
        for (int i = 0; i < m; i++) {
            final Point[] tmp = new Point[4];
            tmp[0] = x[i];
            fillByUniquePoints(1, 4, tmp);
            final int R = random.nextInt(n);
            double[] newY = new double[n];
            for (int j = 0; j < n; j++) {
                double r = unif(0, 1);
                if(r < CR || j == R){
                    newY[j] = tmp[1].getCoordinates()[j] + F*(tmp[2].getCoordinates()[j] - tmp[3].getCoordinates()[j]);
                } else {
                    newY = x[i].getCoordinates();
                }
            }
            Point y = new Point(newY, n);
            if(fitness(y) < fitnesses[i]){
                x[i] = y;
                fitnesses[i] = fitness(y);
            }
        }
    }

    /**
     * find point with better fitness
     * @return - found point
     */
    public Point getBetterPoint(){
        double fitness = fitnesses[0];
        Point better = x[0];
        for (int i = 1; i < m; i++) {
            if(fitness > fitnesses[i]){
                better = x[i];
                fitness = fitnesses[i];
            }
        }
        return better;
    }

    /**
     * find better fitness
     * @return - found fitnes
     */
    public double getBetterFitness(){
        double fitness = fitnesses[0];
        for (int i = 1; i < m; i++) {
            if(fitness > fitnesses[i]){
                fitness = fitnesses[i];
            }
        }
        return fitness;
    }

    /**
     * Uniform distribution (continuous); a < b
     * @param a - left boundary
     * @param b - right boundary
     * @return 0 if x < a, (x-a)/(b-a) if x >=a && x < b, 1 if x >= b
     */
    private double unif(double a, double b) {
        final double x = random.nextDouble();
        if(x < a){
            return 0;
        } else if(x >= b){
            return 1;
        } else {
            return (x-a)/(b-a);
        }
    }

    /**
     * count fitness for one point
     * @param point - point for counting
     * @return value of function (fitness)
     */
    public double fitness(final Point point){
       return 100*Math.pow(point.getCoordinates()[1] - point.getCoordinates()[0]*point.getCoordinates()[0], 2) + Math.pow(1-point.getCoordinates()[0], 2);
    }

    /**
     * create new point with random coordinates and given boundaries of coordinates
     * @return new point
     */
    private Point generateNewRandomPoint() {
        return Point.getRandomPoint(minBoundaries, maxBoundaries, n);
    }

    /**
     * verify if exists Point point in part of array target
     * @param target - array with Points
     * @param currentLength - length of filled part of array
     * @param point - point for verifying
     * @return true or false
     */
    public static boolean isExistPoint(final Point[] target, final int currentLength, final Point point){
        for (int i = 0; i < currentLength; i++) {
            if(point.equals(target[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * verify if exists Point point in array target
     * @param target - array with Points
     * @param point - point for verifying
     * @return true or false
     */
    public static boolean isExistPoint(final Point[] target, final Point point){
        return isExistPoint(target, target.length, point);
    }
}