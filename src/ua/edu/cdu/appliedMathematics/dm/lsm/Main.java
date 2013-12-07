package ua.edu.cdu.appliedMathematics.dm.lsm;

public class Main {

    public static void main(String[] args){

        final double x[] = makeXVector(100);
        final double y[] = makeFunctionResults(x);
        final double[] result = LeastSquareMethod.lsmGeneral(x, y, 2);

        System.out.println("Coefficients:");
        printDoubleVector(result);

        /*
        final double x[] = makeXVector(100);
        final double y[] = makeSimpleFunctionResults(x);
        final double[] result = LeastSquareMethod.simpleLSM(x, y);

        System.out.println("Coefficients:");
        printDoubleVector(result);*/
    }

    /**
     * print vector of double values
     * @param vector - double values
     */
    private static void printDoubleVector(final double[] vector) {
        for (final double aVector : vector) {
            System.out.printf("%.2f\t", aVector);
        }
        System.out.println();
    }

    /**
     * make vector of y params for input x params
     * use function 2*x^2 + 7*x - 5 + rand()
     * @param x - input params
     * @return
     */
    private static  double[] makeFunctionResults(double[] x){
        final double[] y = new double[x.length];
        for(int i = 0; i < x.length; i++){
            y[i] = 2*x[i]*x[i] + 7*x[i] - 5 + Math.random();
        }
        return y;
    }

    /**
     * make vector of y params for input x params
     * use function 2*x - 5 + rand()
     * @param x - input params
     * @return
     */
    private static  double[] makeSimpleFunctionResults(double[] x){
        final double[] y = new double[x.length];
        for(int i = 0; i < x.length; i++){
            y[i] = 2*x[i] - 5 + Math.random();
        }
        return y;
    }

    /**
     * make vector of x
     * @param length - length of new vector
     * @return new vector
     */
    private static double[] makeXVector(final int length) {
        final double[] array = new double[length];
        for(int i = 0; i < length; i++){
            array[i] = i;
        }
        return array;
    }
}
