package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.amoebaOptimization;

import java.util.Arrays;

public class Amoeba {
    private int amoebaSize;  // number of solutions
    private int dim;         // vector-solution size, also problem dimension
    private Solution[] solutions;  // potential solutions (vector + value)

    private double alpha;  // reflection
    private double beta;   // contraction
    private double gamma;  // expansion

    private int maxLoop;   // limits main solving loop

    public Amoeba(int amoebaSize, int dim, double minX, double maxX, int maxLoop) {
        this.amoebaSize = amoebaSize;
        this.dim = dim;
        this.alpha = 1.0;  // hard-coded values from theory
        this.beta = 0.5;
        this.gamma = 2.0;

        this.maxLoop = maxLoop;

        this.solutions = new Solution[amoebaSize];
        for (int i = 0; i < solutions.length; ++i){
            solutions[i] = new Solution(dim, minX, maxX);  // the Solution ctor calls the objective function to compute value
        }
        Arrays.sort(solutions);
    }

    /**
     * return the centroid of all solution vectors except for the worst (highest index) vector
     */
    public Solution centroid() {
        final double[] c = new double[dim];
        for (int i = 0; i < amoebaSize - 1; ++i){
            for (int j = 0; j < dim; ++j){
                c[j] += solutions[i].getVector()[j];  // accumulate sum of each vector component
            }
        }

        for (int j = 0; j < dim; ++j){
            c[j] = c[j] / (amoebaSize - 1);
        }
        // feed vector to ctor which calls objective function to compute value
        return new Solution(c);
    }

    /**
     * the reflected solution extends from the worst (lowest index) solution through the centroid
     */
    public Solution reflected(final Solution centroid) {
        final double[] r = new double[dim];
        final double[] worst = solutions[amoebaSize - 1].getVector();  // convenience only
        for (int j = 0; j < dim; ++j){
            r[j] = ((1 + alpha) * centroid.getVector()[j]) - (alpha * worst[j]);
        }
        return new Solution(r);
    }

    /**
     * expanded extends even more, from centroid, thru reflected
     */
    public Solution expanded(final Solution reflected, final Solution centroid) {
        final double[] e = new double[dim];
        for (int j = 0; j < dim; ++j){
            e[j] = (gamma * reflected.getVector()[j]) + ((1 - gamma) * centroid.getVector()[j]);
        }
        return new Solution(e);
    }

    /**
     * contracted extends from worst (lowest index) towards centoid, but not past centroid
     */
    public Solution contracted(final Solution centroid) {
        final double[] v = new double[dim];  // didn't want to reuse 'c' from centoid routine
        final double[] worst = this.solutions[amoebaSize - 1].getVector();  // convenience only
        for (int j = 0; j < dim; ++j){
            v[j] = (beta * worst[j]) + ((1 - beta) * centroid.getVector()[j]);
        }
        return new Solution(v);
    }

    /**
     * move all vectors, except for the best vector (at index 0), halfway to the best vector
     * compute new objective function values and sort result
     */
    public void shrink() {
        for (int i = 1; i < amoebaSize; ++i){  // note we don't start at [0]
            for (int j = 0; j < dim; ++j) {
                solutions[i].getVector()[j] = (solutions[i].getVector()[j] + solutions[0].getVector()[j]) / 2.0;
                solutions[i].setValue(AmoebaProgram.objectiveFunction(solutions[i].getVector(), null));
            }
        }
        Arrays.sort(solutions);
    }

    /**
     * replace the worst solution (at index size-1) with contents of parameter newSolution's vector
     */
    public void replaceWorst(final Solution newSolution) {
        for (int j = 0; j < dim; ++j){
            solutions[amoebaSize-1].getVector()[j] = newSolution.getVector()[j];
        }
        solutions[amoebaSize - 1].setValue(newSolution.getValue());
        Arrays.sort(solutions);
    }

    /**
     * solve needs to know if the reflected vector is worse (greater value) than every vector in the amoeba, except for the worst vector (highest index)
     */
    public boolean isWorseThanAllButWorst(final Solution reflected) {
        for (int i = 0; i < amoebaSize - 1; ++i) { // not the highest index (worst)
            if (reflected.getValue() <= solutions[i].getValue()){  // reflected is better (smaller value) than at least one of the non-worst solution vectors
                return false;
            }
        }
        return true;
    }

    public Solution solve() {
        int t = 0;  // loop counter
        while (t++ < maxLoop) {
            if (t % 10 == 0) {
                System.out.println("At t = " + t + " curr best solution = " + this.solutions[0]);
            }

            final Solution centroid = centroid();  // compute centroid
            final Solution reflected = reflected(centroid);  // compute reflected

            if (reflected.getValue() < solutions[0].getValue()){  // reflected is better than the curr best
                Solution expanded = expanded(reflected, centroid);  // can we do even better??
                if (expanded.getValue() < solutions[0].getValue()){  // winner! expanded is better than curr best
                    replaceWorst(expanded);  // replace curr worst solution with expanded
                } else {
                    replaceWorst(reflected);  // it was worth a try . . .
                }
                continue;
            }

            if (isWorseThanAllButWorst(reflected)){  // reflected is worse (larger value) than all solution vectors (except possibly the worst one)
                if (reflected.getValue() <= solutions[amoebaSize - 1].getValue()){  // reflected is better (smaller) than the curr worst (last index) vector
                    replaceWorst(reflected);
                }
                final Solution contracted = contracted(centroid);  // compute a point 'inside' the amoeba

                if (contracted.getValue() > solutions[amoebaSize - 1].getValue()){  // contracted is worse (larger value) than curr worst (last index) solution vector
                    shrink();
                } else {
                    replaceWorst(contracted);
                }
                continue;
            }
            replaceWorst(reflected);
        }
        return solutions[0];  // best solution is always at [0]
    }

    public String toString() {
        final StringBuilder s = new StringBuilder();
        for (int i = 0; i < solutions.length; ++i){
            s.append("[").append(i).append("] ").append(solutions[i]);
        }
        return s.toString();
    }
}