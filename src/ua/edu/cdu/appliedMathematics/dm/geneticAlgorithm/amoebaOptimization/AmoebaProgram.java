package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.amoebaOptimization;

/**
 * demo of amoeba method numerical optimization
 * see "A Simplex Method for Function Minimization", J.A. Nelder and R. Mead,
 * The Computer Journal, vol. 7, no. 4, 1965, pp.308-313.
 */
public class AmoebaProgram {

    public static void main(String[] args) {
        System.out.println("Begin amoeba method optimization demo\n");
        System.out.println("Solving Rosenbrock's function f(x,y) = 100*(y-x^2)^2 + (1-x)^2");
        System.out.println("Function has a minimum at x = 1.0, y = 1.0 when f = 0.0\n");

        final int dim = 2;  // problem dimension (number of variables to solve for)
        final int amoebaSize = 3;  // number of potential solutions in the amoeba
        final double minX = -10.0;
        final double maxX = 10.0;
        final int maxLoop = 50;

        System.out.println("Creating amoeba with size = " + amoebaSize);
        System.out.println("Setting maxLoop = " + maxLoop);
        final Amoeba a = new Amoeba(amoebaSize, dim, minX, maxX, maxLoop);  // an amoeba method optimization solver

        System.out.println("\nInitial amoeba is: " + a);

        System.out.println("\nBeginning reflect-expand-contract solve loop\n");
        Solution sln = a.solve();
        System.out.println("\nSolve complete\n");

        System.out.println("Final amoeba is: " + a);

        System.out.println("\nBest solution found: \n");
        System.out.println(sln);

        System.out.println("\nEnd amoeba method optimization demo\n");
    }

    /**
     * Rosenbrock's function, the function to be minimized
     * no data source needed here but real optimization problems will often be based on data
     */
    public static double objectiveFunction(final double[] vector, final Object dataSource) {
        final double x = vector[0];
        final double y = vector[1];
        return 100.0 * Math.pow((y - x * x), 2) + Math.pow(1 - x, 2);
        //return (x * x) + (y * y); // sphere function
    }
}
