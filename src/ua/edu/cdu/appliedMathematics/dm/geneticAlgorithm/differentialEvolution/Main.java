package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.differentialEvolution;

public class Main {

    public static void main(String[] args) {
        //dimension of our task 2. in example used function of Rosenbrock
        //f(x,y) = 100*(y - x^2)^2 + (1 - x)^2
        final int n = 2;
        //5 <= Q <= 10
        final int Q = 5;
        //0 <= F <= 2 or 0.4 <= F <= 1 if different books it is different
        double F = 1;
        //0 <= CR <= 1 probability of mutation
        double CR = 0.7;

        //parameters F and CR we can change on each iteration but the must be in their range
        //changing of this parameters can help when task is bad
        //number of points in each generation = n*Q
        final DE de = new DE(n, n*Q, F, CR, new double[]{-5,-5}, new double[]{5,5});
        for (int i = 0; i < 30; i++) {
            de.solve();
            final Point point = de.getBetterPoint();
            System.out.println("Solution point on step " + i + ": " + point);
            System.out.println("Function value on step " + i + " = " + de.getBetterFitness() + "\n");
            F -= 0.001;
        }

        final Point analyticalPoint = new Point(new double[]{1, 1}, 2);
        System.out.println("Analytical solve point: " + analyticalPoint);
        System.out.println("Analytical solve function value = " + de.fitness(analyticalPoint));
    }
}
