package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.travelingSalesmanProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * realize solving of travel salesman problem
 */
public class TravelingSalesmanProblem {

    private static final Random random = new Random();

    //coordinates of cities of initial generation
    private Point[] cities;
    //coordinates of cities of current generation
    private Point[] parent;
    //coordinates of start and finish city (for cycle way)
    private Point startFinish;
    //generation with smallest distance between cities
    private Generation betterGeneration;
    //all generation
    private List<Generation> generations;

    public TravelingSalesmanProblem(Point[] cities, Point startFinish) {
        this.cities = cities;
        this.betterGeneration = null;
        this.startFinish = startFinish;
        this.generations = new ArrayList<>();
        init();
    }

    /**
     * init solver of problem
     */
    public void init(){
        //make array of points with startFinish point at start and end of array
        parent = makeInitParent();
        //set first generation as better
        betterGeneration = new Generation(countCurrentDistance(parent), parent);
        //add generation to list
        addGeneration(betterGeneration);
    }

    /**
     * add generation to list with other generations and change betterGeneration if new has smaller distance
     * @param generation - new generation
     */
    private void addGeneration(final Generation generation) {
        generations.add(generation);

        betterGeneration = betterGeneration.getDistance() > generation.getDistance() ? generation : betterGeneration;
    }

    /**
     * make cross of generation
     * replace 2 points (1 pair), points selects by random indexes
     * if we have problems and all of new generations worse then current (if i > maxIterations) we change method of crossing:
     * we replace 4 points (2 pairs) or 6 points (3 pairs)
     * if after all iterations we did not find better generation we return false
     * @param maxIterations - max count of iterations, after its we change method of crossing
     * @return true if we found new better generation, false if did not find
     */
    public boolean crossing(final int maxIterations){
        Generation parentGeneration = new Generation(countCurrentDistance(parent), parent);
        Point[] child = crossingOneChild(parent, 1);
        Generation childGeneration = new Generation(countCurrentDistance(child), child);

        int i = 0, j = 1;
        while(parentGeneration.equals(Generation.betterGeneration(parentGeneration, childGeneration)) && i++ < maxIterations){
            child = crossingOneChild(parent, j);
            childGeneration = new Generation(countCurrentDistance(child), child);
            if(i >= maxIterations && j < 4){
                i = 0; j++;
            }
        }
        if(i < maxIterations){
            parent = child;
            addGeneration(childGeneration);
        } else {
            return false;
        }
        return true;
    }

    /**
     * make crossing for Points of current generation using rules from method crossing
     * @param parent - points of current generation
     * @param j - counter of pair for replacing
     * @return coordinates of cities of new generation
     */
    private Point[] crossingOneChild(final Point[] parent, final int j){
        final int length = parent.length;
        final Point[] child = new Point[length];
        System.arraycopy(parent, 0, child, 0, length);
        final int[] idx = makeRandomArrayWithUniqueValues(j*2, length);
        for (int i = 0; i < j * 2; i+=2) {
            Point tmp = child[idx[i]];
            child[idx[i]] = child[idx[i+1]];
            child[idx[i+1]] = tmp;
        }
        return child;
    }

    /**
     * makes array with unique random values (indexes of array with point)
     * @param size - count of indexes of points
     * @param length - length of array with points
     * @return array of indexes
     */
    private int[] makeRandomArrayWithUniqueValues(final int size, final int length){
        final int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            int tmp = random.nextInt(length-2)+1;
            while(exists(array, tmp)){
                tmp = random.nextInt(length-2)+1;
            }
            array[i] = tmp;
        }
        return array;
    }

    /**
     * verify if exists int value in array
     * @param array - array with data
     * @param value - value for verifying
     * @return true of false
     */
    private boolean exists(final int[] array, final int value) {
        for (int anArray : array) {
            if (anArray == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * make init array of coordinates of cities from array {@link cities}
     * this method make first generation
     * @return - array of points of coordinates of cities for first generation
     */
    private Point[] makeInitParent(){
        final int cityCount = cities.length;
        final Point[] parent = new Point[cityCount + 1];
        parent[0] = startFinish; parent[cityCount] = startFinish;
        for (int i = 1; i < cityCount;){
            Point tmp = cities[random.nextInt(cityCount)];
            if(!isExistPoint(parent, tmp)){
                parent[i++] = tmp;
            }
        }
        return parent;
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

    /**
     * counts sum of distances between all cities in current generation
     * @param cities - coordinates of cities
     * @return distance of travel salesman
     */
    private double countCurrentDistance(final Point[] cities){
        double distance = 0;
        for (int i = 0; i < cities.length-1; i++) {
            distance += cities[i+1].distance(cities[i]);
        }
        //return distance;
        return Math.sqrt(distance);
    }

    public List<Generation> getGenerations() {
        return generations;
    }

    public Generation getBetterGeneration() {
        return betterGeneration;
    }
}
