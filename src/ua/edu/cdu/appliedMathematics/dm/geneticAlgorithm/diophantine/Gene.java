package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.diophantine;

import java.util.Arrays;

public class Gene {
    private int[] alleles;
    private int fitness;
    private double likelihood;

    public Gene(int length) {
        alleles = new int[length];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gene gene = (Gene) o;

        return Double.compare(gene.fitness, fitness) == 0 &&
                Double.compare(gene.likelihood, likelihood) == 0 &&
                Arrays.equals(alleles, gene.alleles);
    }

    public int[] getAlleles() {
        return alleles;
    }

    public void setAlleles(int[] alleles) {
        this.alleles = alleles;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }
}
