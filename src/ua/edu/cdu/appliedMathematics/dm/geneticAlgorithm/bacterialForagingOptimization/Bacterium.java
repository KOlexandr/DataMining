package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.bacterialForagingOptimization;

import java.util.Random;

public class Bacterium implements Comparable<Bacterium> {

    public static final String SPACE = " ";

    private double[] position;

    // value we are trying to minimize
    private double prevCost;
    private double cost;

    // cost from intra-bacteria proximity
    //public double swarmCost;
    //public double prevSwarmCost;

    // accumulated measure per swim
    private double health;

    private static Random random = new Random();

    public Bacterium(int dim, double minValue, double maxValue) {

        // random position bacterium
        this.position = new double[dim];
        for (int p = 0; p < dim; ++p) {
            double x = (maxValue - minValue) * random.nextDouble() + minValue;
            this.position[p] = x;
        }

        // costs are due to environment and must be computed externally
        this.health = 0;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("[ ");
        for (double aPosition : this.position) {
            sb.append(aPosition).append(SPACE);
        }
        sb.append("] ");
        sb.append("cost = ").append(this.cost).append(" prevCost = ").append(this.prevCost);
        sb.append(" health = ").append(this.health);
        return sb.toString();
    }

    public int compareTo(Bacterium other){
        if (this.health < other.health){
            return -1;
        } else if (this.health > other.health){
            return 1;
        } else {
            return 0;
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrevCost() {
        return prevCost;
    }

    public void setPrevCost(double prevCost) {
        this.prevCost = prevCost;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }
}
