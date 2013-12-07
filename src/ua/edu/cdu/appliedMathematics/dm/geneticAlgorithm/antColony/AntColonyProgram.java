package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.antColony;

import java.util.Random;

/**
 * Demo of Ant Colony Optimization (ACO) solving a Traveling Salesman Problem (TSP).
 * There are many variations of ACO; this is just one approach.
 * The problem to solve has a program defined number of cities. We assume that every
 * city is connected to every other city. The distance between cities is artificially
 * set so that the distance between any two cities is a random value between 1 and 8
 * Cities wrap, so if there are 20 cities then D(0,19) = D(19,0).
 * Free parameters are alpha, beta, rho, and Q. Hard-coded constants limit min and max
 * values of pheromones.
 */
public class AntColonyProgram {
    private static final Random random = new Random();

    private static final int alpha = 3;           // influence of pheromone on direction
    private static final int beta = 2;            // influence of adjacent node distance

    private static final double rho = 0.01;       // pheromone decrease factor
    private static final double Q = 2.0;          // pheromone increase factor
    public static final String SPACE = " ";

    public static void main(String[] args) {
        System.out.println("Begin Ant Colony Optimization demo\n");

        int numCities = 60;
        int numAnts = 4;
        int maxTime = 1000;

        System.out.println("Number cities in problem = " + numCities);

        System.out.println("\nNumber ants = " + numAnts);
        System.out.println("Maximum time = " + maxTime);

        System.out.println("\nAlpha (pheromone influence) = " + alpha);
        System.out.println("Beta (local node influence) = " + beta);
        System.out.println("Rho (pheromone evaporation coefficient) = " + rho);
        System.out.println("Q (pheromone deposit factor) = " + Q);

        System.out.println("\nInitialing dummy graph distances");
        int[][] distances = makeGraphDistances(numCities);

        System.out.println("\nInitialing ants to random trails\n");
        int[][] ants = initAnts(numAnts, numCities);                // initialize ants to random trails
        showAnts(ants, distances);

        int[] bestTrail = bestTrail(ants, distances);                   // determine the best initial trail
        double bestLength = length(bestTrail, distances);               // the length of the best trail

        System.out.println("\nBest initial trail length: " + bestLength);

        System.out.println("\nInitializing pheromones on trails");
        double[][] pheromones = initPheromones(numCities);

        int time = 0;
        System.out.println("Entering updateAnts - updatePheromones loop");
        while (time++ < maxTime) {
            updateAnts(ants, pheromones, distances);
            updatePheromones(pheromones, ants, distances);

            int[] currBestTrail = bestTrail(ants, distances);
            double currBestLength = length(currBestTrail, distances);
            if (currBestLength < bestLength) {
                bestLength = currBestLength;
                bestTrail = currBestTrail;
                System.out.println("New best length of " + bestLength + " found at time " + time);
            }
        }

        System.out.println("Time complete\n");

        System.out.println("Best trail found:");
        display(bestTrail);
        System.out.println("length of best trail found: " + bestLength);

        System.out.println("End Ant Colony Optimization demo");
    }

    /**
     * init Ants
     */
    private static int[][] initAnts(final int numAnts, final int numCities) {
        final int[][] ants = new int[numAnts][];
        for (int k = 0; k < numAnts; ++k) {
            ants[k] = randomTrail(random.nextInt(numCities), numCities);
        }
        return ants;
    }

    /**
     * helper for initAnts
     */
    private static int[] randomTrail(final int start, final int numCities) {
        final int[] trail = new int[numCities];

        // sequential
        for (int i = 0; i < numCities; ++i) {
            trail[i] = i;
        }

        // Fisher-Yates shuffle
        for (int i = 0; i < numCities; ++i) {
            int r = random.nextInt(numCities-i)+i;
            int tmp = trail[r]; trail[r] = trail[i]; trail[i] = tmp;
        }

        int idx = indexOfTarget(trail, start); // put start at [0]
        int temp = trail[0];
        trail[0] = trail[idx];
        trail[idx] = temp;

        return trail;
    }

    /**
     * helper for randomTrail
     */
    private static int indexOfTarget(final int[] trail, final int target) {
        for (int i = 0; i < trail.length; ++i) {
            if (trail[i] == target){
                return i;
            }
        }
        throw new RuntimeException("Target not found in indexOfTarget");
    }

    /**
     * total length of a trail
     */
    private static double length(final int[] trail, final int[][] distances) {
        double result = 0.0;
        for (int i = 0; i < trail.length - 1; ++i){
            result += distance(trail[i], trail[i + 1], distances);
        }
        return result;
    }

    /**
     * best trail has shortest total length
     */
    private static int[] bestTrail(final int[][] ants, final int[][] distances){
        double bestLength = length(ants[0], distances);
        int idxBestLength = 0;
        for (int k = 1; k < ants.length; ++k) {
            final double len = length(ants[k], distances);
            if (len < bestLength) {
                bestLength = len;
                idxBestLength = k;
            }
        }
        final int numCities = ants[0].length;
        final int[] bestTrail = new int[numCities];
        System.arraycopy(ants[idxBestLength], 0, bestTrail, 0, bestTrail.length);
        return bestTrail;
    }

    private static double[][] initPheromones(final int numCities) {
        final double[][] pheromones = new double[numCities][];
        for (int i = 0; i < numCities; ++i){
            pheromones[i] = new double[numCities];
        }
        for (int i = 0; i < pheromones.length; ++i){
            for (int j = 0; j < pheromones[i].length; ++j){
                pheromones[i][j] = 0.01; // otherwise first call to updateAnts -> buildTrail -> NextNode -> moveProbes => all 0.0 => throws
            }
        }
        return pheromones;
    }

    private static void updateAnts(final int[][] ants, final double[][] pheromones, final int[][] distances) {
        final int numCities = pheromones.length;
        for (int k = 0; k < ants.length; ++k) {
            ants[k] = buildTrail(random.nextInt(numCities), pheromones, distances);
        }
    }

    private static int[] buildTrail(final int start, final double[][] pheromones, final int[][] distances) {
        final int numCities = pheromones.length;
        final int[] trail = new int[numCities];
        final boolean[] visited = new boolean[numCities];
        trail[0] = start;
        visited[start] = true;
        for (int i = 0; i < numCities - 1; ++i) {
            int cityX = trail[i];
            int next = nextCity(cityX, visited, pheromones, distances);
            trail[i + 1] = next;
            visited[next] = true;
        }
        return trail;
    }

    private static int nextCity(final int cityX, final boolean[] visited, final double[][] pheromones, final int[][] distances) {
        // for ant k (with visited[]), at nodeX, what is next node in trail?
        final double[] probes = moveProbes(cityX, visited, pheromones, distances);

        final double[] cumul = new double[probes.length + 1];
        for (int i = 0; i < probes.length; ++i){
            cumul[i + 1] = cumul[i] + probes[i]; // consider setting cumul[cuml.length-1] to 1.00
        }
        final double p = random.nextDouble();
        for (int i = 0; i < cumul.length - 1; ++i){
            if (p >= cumul[i] && p < cumul[i + 1]){
                return i;
            }
        }
        throw new RuntimeException("Failure to return valid city in nextCity");
    }

    private static double[] moveProbes(final int cityX, final boolean[] visited, final double[][] pheromones, final int[][] distances) {
        // for ant k, located at nodeX, with visited[], return the prob of moving to each city
        final int numCities = pheromones.length;
        final double[] taueta = new double[numCities]; // includes cityX and visited cities
        double sum = 0; // sum of all tauetas

        for (int i = 0; i < taueta.length; ++i){ // i is the adjacent city
            if (i == cityX) {
                taueta[i] = 0; // prob of moving to self is 0
            } else if (visited[i]) {
                taueta[i] = 0; // prob of moving to a visited city is 0
            } else {
                taueta[i] = Math.pow(pheromones[cityX][i], alpha) * Math.pow((1.0 / distance(cityX, i, distances)), beta); // could be huge when pheromone[][] is big
                if (taueta[i] < 0.0001) {
                    taueta[i] = 0.0001;
                } else if (taueta[i] > (Double.MAX_VALUE / (numCities * 100))){
                    taueta[i] = Double.MAX_VALUE / (numCities * 100);
                }
            }
            sum += taueta[i];
        }

        final double[] probes = new double[numCities];
        for (int i = 0; i < probes.length; ++i){
            probes[i] = taueta[i] / sum; // big trouble if sum = 0.0
        }
        return probes;
    }

    private static void updatePheromones(final double[][] pheromones, final int[][] ants, final int[][] distances) {
        for (int i = 0; i < pheromones.length; ++i) {
            for (int j = i + 1; j < pheromones[i].length; ++j) {
                for (int[] ant : ants) {
                    double length = length(ant, distances); // length of ant k trail
                    double decrease = (1 - rho) * pheromones[i][j];
                    double increase = 0;
                    if (edgeInTrail(i, j, ant)) {
                        increase = (Q / length);
                    }

                    pheromones[i][j] = decrease + increase;

                    if (pheromones[i][j] < 0.0001) {
                        pheromones[i][j] = 0.0001;
                    } else if (pheromones[i][j] > 100000.0) {
                        pheromones[i][j] = 100000.0;
                    }

                    pheromones[j][i] = pheromones[i][j];
                }
            }
        }
    }

    /**
     * are cityX and cityY adjacent to each other in trail[]?
     */
    private static boolean edgeInTrail(final int cityX, final int cityY, final int[] trail) {
        final int lastIndex = trail.length - 1;
        final int idx = indexOfTarget(trail, cityX);

        if (idx == 0 && trail[1] == cityY) {
            return true;
        } else if (idx == 0 && trail[lastIndex] == cityY) {
            return true;
        } else if (idx == 0) {
            return false;
        } else if (idx == lastIndex && trail[lastIndex - 1] == cityY) {
            return true;
        } else if (idx == lastIndex && trail[0] == cityY) {
            return true;
        } else if (idx == lastIndex) {
            return false;
        } else if (trail[idx - 1] == cityY) {
            return true;
        } else if (trail[idx + 1] == cityY) {
            return true;
        } else {
            return false;
        }
    }

    private static int[][] makeGraphDistances(int numCities) {
        final int[][] distances = new int[numCities][];
        for (int i = 0; i < distances.length; ++i){
            distances[i] = new int[numCities];
        }
        for (int i = 0; i < numCities; ++i){
            for (int j = i + 1; j < numCities; ++j){
                int d = random.nextInt(8)+1; // [1,8]
                distances[i][j] = d;
                distances[j][i] = d;
            }
        }
        return distances;
    }

    private static double distance(final int cityX, final int cityY, final int[][] distances) {
        return distances[cityX][cityY];
    }

    private static void display(final int[] trail) {
        for (int i = 0; i < trail.length; ++i) {
            System.out.print(trail[i] + SPACE);
            if (i > 0 && i % 20 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void showAnts(final int[][] ants, final int[][] distances) {
        for (int i = 0; i < ants.length; ++i) {
            System.out.print(i + ": [ ");

            for (int j = 0; j < 4; ++j){
                System.out.print(ants[i][j] + SPACE);
            }

            System.out.print(". . . ");

            for (int j = ants[i].length - 4; j < ants[i].length; ++j){
                System.out.print(ants[i][j] + SPACE);
            }

            System.out.println("] len = " + length(ants[i], distances));
            System.out.println();
        }
    }

    private static void display(final double[][] pheromones) {
        for (int i = 0; i < pheromones.length; ++i) {
            System.out.print(i + ": ");
            for (int j = 0; j < pheromones[i].length; ++j) {
                System.out.print(pheromones[i][j] + SPACE);
            }
            System.out.println();
        }
    }
}