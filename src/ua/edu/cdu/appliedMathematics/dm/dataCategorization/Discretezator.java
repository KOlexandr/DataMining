package ua.edu.cdu.appliedMathematics.dm.dataCategorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Discretezator<T extends Double> {

    public static final String LINE_SEPARATOR = "\n";
    public static final String EMPTY_STRING = "";
    public static final String TAB_CHAR = "\t";
    public static final double EPS = 0.000001;

    private double[] sortedCopyOfDistinctData;
    private int numberOfCluster;                //must be > sortedCopyOfDistinctData.length
    private double[] means;
    private int[] clustering;                   //index = index into sortedCopyOfDistinctData[], value = cluster ID
    private int maxCountOfSteps = 0;

    /**
     * constructor with maxCountOfSteps
     * @param rawData - array of all points
     * @param maxCountOfSteps - max count of steps finding clusters in cycle
     */
    public Discretezator(final double[] rawData, final int maxCountOfSteps) {
        this(rawData);
        this.maxCountOfSteps = maxCountOfSteps;
    }

    public Discretezator(final double[] rawData) {
        //copy input data
        final double[] sortedRawData = Arrays.copyOf(rawData, rawData.length);
        //sort copied data
        Arrays.sort(sortedRawData);
        //get distinct value from sorted data vector
        this.sortedCopyOfDistinctData = getDistinctValues(sortedRawData);
        //create new int array for number of cluster for distinct value
        this.clustering = new int[sortedCopyOfDistinctData.length];

        //count number of all clusters as ceil(sortedCopyOfDistinctData.length)^(1/2)
        this.numberOfCluster = (int)Math.sqrt(sortedCopyOfDistinctData.length);
        //create new double vector for centroids (means)
        this.means = new double[numberOfCluster];
        this.cluster();
    }

    /**
     * initialize all and find clusters
     */
    private void cluster(){
        //initialize number of cluster for all points
        initializeClustering();
        //count first means for all points
        computeMeans();

        boolean changed = true;
        boolean success = true;
        int countOfSteps = 0;
        //some max count of steps for while cycle
        //you can change this value if you want
        if(maxCountOfSteps <= 0){
            maxCountOfSteps = sortedCopyOfDistinctData.length * 10;
        }
        //while any of point not assigned to another cluster and
        //all clusters has one or more points
        //and countOfSteps < maxCountOfSteps
        while (changed && success && countOfSteps < maxCountOfSteps){
            //increment steps
            countOfSteps++;
            //verify if changed cluster numbers for any value
            changed = assignAll();
            //count new means and verify
            success = computeMeans();
        }
    }

    /**
     * for any value x, compute distance to each cluster mean
     * @param x - test point
     * @return closest index, which is a cluster, which is the category
     */
    public int discretize(final T x) {
        final double[] distances = new double[numberOfCluster];
        for (int i = 0; i < numberOfCluster; i++){
            distances[i] = distance(x, means[i]);
        }
        return findIndexOfMinValue(distances);
    }

    /**
     * find all distinct points
     * @param array all points
     * @return distinct points
     */
    private static double[] getDistinctValues(final double[] array) {
        final List<Double> distinctList = new ArrayList<>();
        distinctList.add(array[0]);
        for (int i = 0; i < array.length-1; i++){
            if (!isEqual(array[i], array[i + 1])){
                distinctList.add(array[i + 1]);
            }
        }
        return listToArray(distinctList);
    }

    /**
     * transform list of <T> to double array
     * @param list
     * @param <T> extends Double
     * @return double array
     */
    private static <T extends Double> double[] listToArray(final List<T> list) {
        final double[] array = new double[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i).doubleValue();
        }
        return array;
    }

    /**
     * verify if two points equals
     * @param x1 - first point
     * @param x2 - second point
     * @param <T> extends Number
     * @return true if distance between two points < than EPS
     */
    private static <T extends Number> boolean isEqual(final T x1, final T x2) {
        return Math.abs(x1.doubleValue() - x2.doubleValue()) < EPS;
    }

    /**
     * initialize array with number of cluster for all points from input data
     */
    private void initializeClustering(){
        final int[] initialIndexes = getInitialIndexes();
        for (int i = 0; i < sortedCopyOfDistinctData.length; i++){
            clustering[i] = initialCluster(i, initialIndexes);
        }
    }

    /**
     * count index of start of all clusters
     * @return sorted array of indexes
     */
    private int[] getInitialIndexes(){
        //count interval for any of clusters
        final int interval = sortedCopyOfDistinctData.length / numberOfCluster;
        final int[] result = new int[numberOfCluster];
        for (int i = 0; i < numberOfCluster; i++){
            result[i] = interval * (i + 1);
        }
        return result;
    }

    /**
     * find number of cluster which has point with index
     * @param index - current index of point
     * @param initialIndexes - sorted array of indexes
     * @return number of cluster
     */
    private int initialCluster(final int index, final int[] initialIndexes){
        for (int i = 0; i < initialIndexes.length; i++){
            if (index < initialIndexes[i]){
                return i;
            }
        }
        return initialIndexes.length - 1; // last cluster
    }

    /**
     * count all new means
     * @return false if at some point a count goes to zero
     */
    private boolean computeMeans(){
        final double[] sums = new double[numberOfCluster];
        final int[] counts = new int[numberOfCluster];

        for (int i = 0; i < sortedCopyOfDistinctData.length; i++){
            int tmpIdx = clustering[i];
            sums[tmpIdx] += sortedCopyOfDistinctData[i];
            counts[tmpIdx]++;
        }
        for (int i = 0; i < sums.length; i++){
            if (counts[i] == 0){
                return false;
            } else {
                sums[i] = sums[i] / counts[i];
            }
        }
        means = sums;
        return true;
    }

    /**
     * verify if any of points assigned to another cluster
     * means - cluster centroids
     * clustering - number of cluster for all points before
     * @return true if any changed, false another way
     */
    private boolean assignAll(){
        boolean changed = false;
        final double[] distances = new double[numberOfCluster]; // distance to each cluster mean
        for (int i = 0; i < sortedCopyOfDistinctData.length; i++){
            for (int j = 0; j < numberOfCluster; j++){
                distances[j] = distance(sortedCopyOfDistinctData[i], means[j]);
            }
            int newCluster = findIndexOfMinValue(distances);
            if (newCluster != clustering[i]){
                changed = true;
                clustering[i] = newCluster;
            }
        }
        return changed;
    }

    /**
     * find minimum value in array and return his index
     * @param array array of double values
     * @return
     */
    private int findIndexOfMinValue(final double[] array){
        int indexOfMin = 0;
        double min = array[0];
        for (int i = 0; i < array.length; i++){
            if (array[i] < min){
                min = array[i];
                indexOfMin = i;
            }
        }
        return indexOfMin;
    }

    /**
     * count distance between two points
     * @param x1 - first coordinate
     * @param x2 - second coordinate
     * @param <T> - type extends Number
     * @return distance ((x1-x2)*(x1-x2))^(1/2)
     */
    private static <T extends Number> double distance(final T x1, final T x2) {
        return Math.sqrt((x1.doubleValue() - x2.doubleValue()) * (x1.doubleValue() - x2.doubleValue()));
    }

    public String toString(){
        String string = EMPTY_STRING;
        string += "Distinct data:" + LINE_SEPARATOR;
        for (int i = 0; i < sortedCopyOfDistinctData.length; ++i){
            if ((i+1) % 10 == 0){
                string += LINE_SEPARATOR;
            }
            string += sortedCopyOfDistinctData[i] + TAB_CHAR;
        }
        string += LINE_SEPARATOR + "numberOfCluster = " + numberOfCluster;
        string += LINE_SEPARATOR + "Clustering:" + LINE_SEPARATOR;
        for (final int aClustering : clustering) {
            string += aClustering + TAB_CHAR;
        }
        string += LINE_SEPARATOR + "Means:" + LINE_SEPARATOR;
        for (final double mean : means) {
            string += mean + TAB_CHAR;
        }
        return string;
    }
}