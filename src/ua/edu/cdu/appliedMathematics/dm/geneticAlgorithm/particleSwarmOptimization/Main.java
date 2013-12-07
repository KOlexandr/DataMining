package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.particleSwarmOptimization;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final int dim = 2;                                          // dimensions
        final double maxX = 100.0;
        final double minX = -100.0;
        final int numberParticles = 10;
        final int numberIterations = 1000;
        final Random random = new Random(0);

        System.out.println("\nBegin Particle Swarm Optimization demonstration\n");
        System.out.println("Objective function to minimize has dimension = " + dim);
        System.out.println("Objective function is f(x) = 3 + (x0^2 + x1^2)");

        System.out.println("Range for all x values is " + minX + " <= x <= " + maxX + "\n");
        System.out.println("Number iterations = " + numberIterations);
        System.out.println("Number particles in swarm = " + numberParticles);

        Particle[] swarm = new Particle[numberParticles];
        double[] bestGlobalPosition = new double[dim];              // best solution found by any particle in the swarm. implicit initialization to all 0.0
        double bestGlobalFitness = Double.MAX_VALUE;                // smaller values better

        System.out.println("\nInitializing swarm with random positions/solutions");
        for (int i = 0; i < swarm.length; ++i) {                    // initialize each Particle in the swarm
            double[] randomPosition = new double[dim];
            for (int j = 0; j < randomPosition.length; ++j) {
                randomPosition[j] = (maxX - minX) * random.nextDouble() + minX;
            }
            double fitness = objectiveFunction(randomPosition);
            double[] randomVelocity = new double[dim];

            for (int j = 0; j < randomVelocity.length; ++j) {
                double lo = -1.0 * Math.abs(maxX - minX);
                double hi = Math.abs(maxX - minX);
                randomVelocity[j] = (hi - lo) * random.nextDouble() + lo;
            }
            swarm[i] = new Particle(randomPosition, fitness, randomVelocity, randomPosition, fitness);

            // does current Particle have global best position/solution?
            if (swarm[i].getFitness() < bestGlobalFitness) {
                bestGlobalFitness = swarm[i].getFitness();
                bestGlobalPosition = swarm[i].getPosition();
            }
        } // initialization

        System.out.println("\nInitialization complete");
        System.out.println("Initial best fitness = " + bestGlobalFitness);
        System.out.println("Best initial position/solution:");
        for (int i = 0; i < bestGlobalPosition.length; ++i) {
            System.out.println("x" + i + " = " + bestGlobalPosition[i] + " ");
        }

        double w = 0.729;                                                   // inertia weight. see http://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=00870279
        double c1 = 1.49445;                                                // cognitive/local weight
        double c2 = 1.49445;                                                // social/global weight
        double r1, r2;                                                      // cognitive and social randomizations

        System.out.println("\nEntering main PSO processing loop");
        for (int iteration = 0; iteration < numberIterations; iteration++) {
            double[] newVelocity = new double[dim];
            double[] newPosition = new double[dim];
            double newFitness;

            for (Particle currP : swarm) {                                  // each Particle
                for (int j = 0; j < currP.getVelocity().length; ++j) {      // each x value of the velocity
                    r1 = random.nextDouble();
                    r2 = random.nextDouble();

                    newVelocity[j] = (w * currP.getVelocity()[j]) +
                            (c1 * r1 * (currP.getBestPosition()[j] - currP.getBestPosition()[j])) +
                            (c2 * r2 * (bestGlobalPosition[j] - currP.getBestPosition()[j]));

                    if (newVelocity[j] < -maxX){
                        newVelocity[j] = -maxX;
                    } else if (newVelocity[j] > maxX){
                        newVelocity[j] = maxX;
                    }
                }

                currP.setVelocity(newVelocity);

                for (int j = 0; j < currP.getPosition().length; ++j) {
                    newPosition[j] = currP.getPosition()[j] + newVelocity[j];
                    if (newPosition[j] < minX){
                        newPosition[j] = minX;
                    } else if (newPosition[j] > maxX){
                        newPosition[j] = maxX;
                    }
                }

                currP.setPosition(newPosition);
                newFitness = objectiveFunction(newPosition);
                currP.setFitness(newFitness);

                if (newFitness < currP.getBestFitness()) {
                    currP.setBestPosition(newPosition);
                    currP.setBestFitness(newFitness);
                }

                if (newFitness < bestGlobalFitness) {
                    bestGlobalPosition = newPosition;
                    bestGlobalFitness = newFitness;
                }
            }
            System.out.println(swarm[0].toString());
        }
        System.out.println("Processing complete");
        System.out.println("Final best fitness = " + bestGlobalFitness);
        System.out.println("Best position/solution:");
        for (int i = 0; i < bestGlobalPosition.length; ++i) {
            System.out.println("x" + i + " = " + bestGlobalPosition[i]);
        }
        System.out.println("\nEnd PSO demonstration\n");
    }

    private static double objectiveFunction(double[] x) {
        return 3.0 + (x[0] * x[0]) + (x[1] * x[1]); // f(x) = 3 + x^2 + y^2
    }
}
