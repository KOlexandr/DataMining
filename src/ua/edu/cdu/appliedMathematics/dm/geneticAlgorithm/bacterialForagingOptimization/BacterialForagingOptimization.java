package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.bacterialForagingOptimization;

import java.util.Arrays;
import java.util.Random;

/**
 * Demonstration of an algorithm based on the behavior of E. coli bacteria to solve
 * a difficult minimization problem called the Rastrigin function.
 */
public class BacterialForagingOptimization {
    public static final String SPACE = " ";
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println("Begin Bacterial Foraging Optimization demo\n");
        System.out.println("Target function to minimize: f(x,y) = (x^2 - 10*cos(2*PI*x)) + (y^2 - 10*cos(2*PI*y)) + 20");
        System.out.println("Target function has known optimum value = 0.0 at x = 0.0 and y = 0.0");

        final int dim = 2;                    //problem dimension ("p")
        final double minValue = -5.12;
        final double maxValue = 5.12;
        final int colonySize = 100;           //colony size; evenly divisible by 2
        final int nc = 20;                    //chemotactic steps, index = j
        final int ns = 5;                     //maximum swim steps, index = m
        final int nre = 8;                    //reproduction steps, index = k
        final int ned = 4;                    //dispersal steps, index = l ('el')
        final double ped = 0.25;              //probability of dispersal
        final double ci = 0.05;               //basic bacteria movement increment size (for all bacteria)

        System.out.println("\nColony size = " + colonySize);
        System.out.println("Chemotactic step count = " + nc);
        System.out.println("Reproduction step count = " + nre);
        System.out.println("Dispersal step count = " + ned);
        System.out.println("Maximum swim step count = " + ns);
        System.out.println("Death probability = " + ped);
        System.out.println("Base swim length = " + ci);

        // initialize bacteria colony
        System.out.println("\nInitializing bacteria colony to random positions");
        // initialize a colony of bacteria at random positions (but not the costs)
        Colony colony = new Colony(colonySize, dim, minValue, maxValue);
        System.out.println("Computing the cost value for each bacterium");
        for (int i = 0; i < colonySize; ++i) {// costs
            double cost = cost(colony.getBacteria()[i].getPosition()); // compute cost
            colony.getBacteria()[i].setCost(cost);
            colony.getBacteria()[i].setPrevCost(cost);
        }

        // find best initial cost and position (could have done this with the init loop)
        double bestCost = colony.getBacteria()[0].getCost();
        int indexOfBest = 0;
        for (int i = 0; i < colonySize; ++i) { if (colony.getBacteria()[i].getCost() < bestCost) {
            bestCost = colony.getBacteria()[i].getCost(); indexOfBest = i; }
        }
        double[] bestPosition = new double[dim];
        System.arraycopy(colony.getBacteria()[indexOfBest].getPosition(), 0, bestPosition, 0, dim);

        System.out.println("\nBest initial cost = " + bestCost);

        // main processing
        System.out.println("\nEntering main BFO tumble-swim-reproduce-disperse algorithm loop\n");

        // time counter
        int t = 0;
        // eliminate-disperse loop
        for (int l = 0; l < ned; ++l) {
            // reproduce-eliminate loop
            for (int k = 0; k < nre; ++k) {
                // chemotactic loop; the lifespan of each bacterium
                for (int j = 0; j < nc; ++j) {
                    // reset the health of each bacterium to 0.0
                    for (int i = 0; i < colonySize; ++i) {
                        colony.getBacteria()[i].setHealth(0);
                    }

                    // each bacterium
                    for (int i = 0; i < colonySize; ++i) {
                        // tumble (point in a new direction)
                        double[] tumble = new double[dim];

                        // (hi - lo) * r + lo => random i [-1, +1]
                        for (int p = 0; p < dim; ++p) {
                            tumble[p] = 2.0 * random.nextDouble() - 1.0;
                        }
                        double rootProduct = 0;
                        for (int p = 0; p < dim; ++p) {
                            rootProduct += (tumble[p] * tumble[p]);
                        }

                        // move in new direction
                        for (int p = 0; p < dim; ++p) {
                            colony.getBacteria()[i].getPosition()[p] += (ci * tumble[p]) / rootProduct;
                        }

                        // update costs of new position
                        colony.getBacteria()[i].setPrevCost(colony.getBacteria()[i].getPrevCost());
                        colony.getBacteria()[i].setCost(cost(colony.getBacteria()[i].getPosition()));
                        // health is an accumulation of costs during bacterium's life
                        colony.getBacteria()[i].setHealth(colony.getBacteria()[i].getHealth() + colony.getBacteria()[i].getCost());

                        // new best?
                        if (colony.getBacteria()[i].getCost() < bestCost) {
                            System.out.println("New best solution found by bacteria " + i + " at time = " + t);
                            bestCost = colony.getBacteria()[i].getCost();
                            System.arraycopy(colony.getBacteria()[i].getPosition(), 0, bestPosition, 0, dim);
                        }

                        // swim or not based on prev and curr costs
                        int m = 0;
                        // we are improving
                        while (m++ < ns && colony.getBacteria()[i].getCost() < colony.getBacteria()[i].getPrevCost()) {
                            // move in current direction
                            for (int p = 0; p < dim; ++p) {
                                colony.getBacteria()[i].getPosition()[p] += (ci * tumble[p]) / rootProduct;
                            }

                            // update costs
                            colony.getBacteria()[i].setPrevCost(colony.getBacteria()[i].getCost());
                            colony.getBacteria()[i].setCost(cost(colony.getBacteria()[i].getPosition()));

                            // did we find a new best?
                            if (colony.getBacteria()[i].getCost() < bestCost) {
                                System.out.println("New best solution found by bacteria " + i + " at time = " + t);
                                bestCost = colony.getBacteria()[i].getCost();
                                System.arraycopy(colony.getBacteria()[i].getPosition(), 0, bestPosition, 0, dim);
                            }
                        } // while improving
                    } // i, each bacterium in the chemotactic loop
                    ++t;   // increment the time counter
                } // j, chemotactic loop

                // reproduce the healthiest half of bacteria, eliminate the other half
                Arrays.sort(colony.getBacteria());                      // sort from smallest health (best) to highest health (worst)
                // left points to a bacterium that will reproduce
                for (int left = 0; left < colonySize / 2; ++left) {
                    // right points to a bad bacterium in the right side of array that will die
                    int right = left + colonySize / 2;
                    System.arraycopy(colony.getBacteria()[right].getPosition(), 0, colony.getBacteria()[left].getPosition(), 0, colony.getBacteria()[left].getPosition().length);
                    colony.getBacteria()[right].setCost(colony.getBacteria()[left].getCost());
                    colony.getBacteria()[right].setPrevCost(colony.getBacteria()[left].getPrevCost());
                    colony.getBacteria()[right].setHealth(colony.getBacteria()[left].getHealth());
                }
            } // k, reproduction loop

            // eliminate-disperse
            for (int i = 0; i < colonySize; ++i) {
                double prob = random.nextDouble();
                if (prob < ped) {// disperse this bacterium to a random position
                    for (int p = 0; p < dim; ++p) {
                        double x = (maxValue - minValue) * random.nextDouble() + minValue;
                        colony.getBacteria()[i].getPosition()[p] = x;
                    }
                    // update costs
                    // compute
                    double cost = cost(colony.getBacteria()[i].getPosition());
                    colony.getBacteria()[i].setCost(cost);
                    colony.getBacteria()[i].setPrevCost(cost);
                    colony.getBacteria()[i].setHealth(0.0);

                    // new best by pure luck?
                    if (colony.getBacteria()[i].getCost() < bestCost) {
                        System.out.println("New best solution found by bacteria " + i + " at time = " + t);
                        bestCost = colony.getBacteria()[i].getCost();
                        System.arraycopy(colony.getBacteria()[i].getPosition(), 0, bestPosition, 0, dim);
                    }
                }
            }
        } // l, elimination-dispersal loop, end processing

        System.out.println("\n\nAll BFO processing complete");
        System.out.println("\nBest cost (minimum function value) found = " + bestCost);
        System.out.print("Best position/solution = ");
        showVector(bestPosition);

        System.out.println("\nEnd BFO demo\n");
    }

    /**
     * print vector
     */
    private static void showVector(final double[] vector) {
        System.out.print("[ ");
        for (double aVector : vector) {
            System.out.print(aVector + SPACE);
        }
        System.out.println("]"); 
    }

    /**
     * the cost function we are trying to minimize
     */
    private static double cost(final double[] position){
        // Rastrigin function. f(x,y) = (x^2 - 10*cos(2*PI*x)) + (y^2 - 10*cos(2*PI*y)) + 20
        double result = 0.0;
        for (double xi : position) {
            result += (xi * xi) - (10 * Math.cos(2 * Math.PI * xi)) + 10;
        }
        return result;
    }
}
