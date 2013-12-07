package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.amoebaOptimization;

import java.util.Random;

/**
 * a potential solution (array of double) and associated value (so can be sorted against several potential solutions
 */
public class Solution implements Comparable<Solution> {
    public static final String SPACE = " ";

    private double[] vector;
    private double value;

    private static final Random random = new Random();  // to allow creation of random solutions

    public Solution(int dim, double minX, double maxX) {
        // a random Solution
        this.vector = new double[dim];
        for (int i = 0; i < dim; ++i){
            this.vector[i] = (maxX - minX) * random.nextDouble() + minX;
        }
        this.value = AmoebaProgram.objectiveFunction(this.vector, null);
    }

    public Solution(double[] vector) {
        // a specified solution
        this.vector = vector;
        this.value = AmoebaProgram.objectiveFunction(this.vector, null);
    }

    /**
     * based on vector/solution value
     */
    public int compareTo(Solution other) {
        if (this.value < other.value){
            return -1;
        } else if (this.value > other.value) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("[ ");
        for (double aVector : vector) {
            if (aVector >= 0.0) {
                sb.append(SPACE);
            }
            sb.append(aVector).append(SPACE);
        }
        sb.append("]  val = ").append(value);
        return sb.toString();
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
