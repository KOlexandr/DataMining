package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.diophantine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiophantineSolver {

    private static final int MAX_POPULATIONS_COUNT = 50;
    private static final Random random = new Random();
    private int[] coefficients;
    private int result;
    private List<Gene> population;

    public DiophantineSolver(int[] coefficients, int result) {
        this.result = result;
        this.coefficients = coefficients;
        this.population = new ArrayList<>();
    }

    public int solve(){
        int fitness;
        //generate initial population
        for (int i = 0; i < MAX_POPULATIONS_COUNT; i++) { //fill population by valuer from 0 to result
            Gene g = new Gene(coefficients.length);
            for (int j = 0; j < coefficients.length; j++) {
                g.getAlleles()[j] = random.nextInt(result+1);
            }
            population.add(g);
        }
        fitness = createFitnesses();
        if(fitness != -1){
            return fitness;
        }
        int iterations = 0;
        while(fitness != 0 || ++iterations < 50){ // while we not find solution or over 50 iterations
            generateLakelihoods();
            createNewPopulation();
            fitness = createFitnesses();
            if(fitness != -1){
                return fitness;
            }
        }
        return -1;
    }

    private int fitness(final Gene g){
        int total = 0;
        for (int i = 0; i < coefficients.length; i++) {
            total += coefficients[i]*g.getAlleles()[i];
        }
        g.setFitness(Math.abs(total - result));
        return g.getFitness();
    }

    private int createFitnesses(){
        for (int i = 0; i < population.size(); i++) {
            int fitness = fitness(population.get(i));
            if(fitness == 0){
                return i;
            }
        }
        return -1;
    }

    private double multInv(){
        double sum = 0;
        for (Gene aPopulation : population) {
            sum += 1.0 / (aPopulation.getFitness());
        }
        return sum;
    }

    private void generateLakelihoods(){
        double multInv = multInv();
        double last = 0;
        for (Gene aPopulation : population) {
            last += ((1.0 / (aPopulation.getFitness()) / multInv) * 100);
            aPopulation.setLikelihood(last);
        }
    }

    private int getIndex(final double value){
        double last = 0;
        for (int i = 0; i < population.size(); i++) {
            if(last <= value && value <= population.get(i).getLikelihood()){
                return i;
            } else {
                last = population.get(i).getLikelihood();
            }
        }
        return 4;
    }

    private Gene breed(final int p1, final int p2){
        int crossover = random.nextInt(coefficients.length-1) + 1; //create crossover point (not first)
        int first = random.nextInt(100);
        Gene child = population.get(p1); //Child is all first parent initially

        int initial = 0, fin = coefficients.length-1; //boundaries of crossover
        if(first < 50){ //if first parent first start from crossover
            initial = crossover;
        } else { //else end at crossover
            fin = crossover+1;
        }
        for (int i = initial; i < fin; i++) {//crossover
            child.getAlleles()[i] = population.get(p2).getAlleles()[i];
            if(random.nextInt(101) > 5){
                child.getAlleles()[i] = random.nextInt(result+1);
            }
        }
        return child; //return new kid
    }

    private void createNewPopulation(){
        Gene g[] = new Gene[population.size()];
        for (int i = 0; i < population.size(); i++) {
            int parent1 = 0, parent2 = 0, iterations = 0;
            while((parent1 == parent1 || population.get(parent1).equals(population.get(parent2))) && iterations++ < 25){
                parent1 = getIndex(random.nextInt(101));
                parent2 = getIndex(random.nextInt(101));
            }
            g[i] = breed(parent1, parent2);
        }
        for (int i = 0; i < population.size(); i++) {
            population.set(i, g[i]);
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Gene getGene(final int idx){
        return population.get(idx);
    }
}
