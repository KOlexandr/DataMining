package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.differentialEvolution;

public class Main {

    public static void main(String[] args) {
        final int n = 2;
        //5 <= Q <=10
        final int Q = 5;
        //0 <= F <= 2
        final double F = 1.9;
        //0<= CR <= 1
        final double CR = 0.7;

        final DE de = new DE(n, n*Q, F, CR, new double[]{-5,-5}, new double[]{5,5});
        for (int i = 0; i < 30; i++) {
            de.solve();
            final Point point = de.getBetterPoint();
            System.out.println("Solution point on step " + i + ": " + point);
            System.out.println("Function value on step " + i + " = " + de.getBetterFitness() + "\n");
        }

        final Point analyticalPoint = new Point(new double[]{1, 1}, 2);
        System.out.println("Analytical solve point: " + analyticalPoint);
        System.out.println("Analytical solve function value = " + de.fitness(analyticalPoint));
    }
}
