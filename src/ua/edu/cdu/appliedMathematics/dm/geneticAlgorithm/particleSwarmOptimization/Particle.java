package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.particleSwarmOptimization;

import java.text.DecimalFormat;

public class Particle {
    private static final String SEPARATOR = "\t";
    private double[] position; // equivalent to x-Values and/or solution
    private double fitness;
    private double[] velocity;

    private double[] bestPosition; // best position found so far by this Particle
    private double bestFitness;

    public Particle(double[] position, double fitness, double[] velocity, double[] bestPosition, double bestFitness) {
        this.position = position;
        this.fitness = fitness;
        this.velocity = velocity;
        this.bestPosition = bestPosition;
        this.bestFitness = bestFitness;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final DecimalFormat df = new DecimalFormat("#.##");
        sb.append("====================================\n");
        sb.append("Position: ");
        for (double aPosition : position) {
            sb.append(df.format(aPosition)).append(SEPARATOR);
        }
        sb.append("\nFitness = ").append(df.format(fitness)).append("\n");
        sb.append("Velocity: ");
        for (double aVelocity : velocity) {
            sb.append(df.format(aVelocity)).append(" ");
        }
        sb.append("\nBest Position: ");
        for (double aBestPosition : bestPosition) {
            sb.append(df.format(aBestPosition)).append(" ");
        }
        sb.append("\nBest Fitness = ").append(df.format(bestFitness)).append("\n");
        sb.append("====================================\n");
        return sb.toString();
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double[] getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(double[] bestPosition) {
        this.bestPosition = bestPosition;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    }
}
