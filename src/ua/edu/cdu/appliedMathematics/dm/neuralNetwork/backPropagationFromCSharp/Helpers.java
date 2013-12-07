package ua.edu.cdu.appliedMathematics.dm.neuralNetwork.backPropagationFromCSharp;

public class Helpers {

    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";

    public static double[][] makeMatrix(final int rows, final int cols) {
        return new double[rows][cols];
    }

    public static void showVector(final double[] vector, final int decimals, final boolean blankLine) {
        for (int i = 0; i < vector.length; ++i) {
            if (i > 0 && i % 12 == 0){ // max of 12 values per row
                System.out.println();
            }
            if (vector[i] >= 0) {
                System.out.print(SPACE);
            }
            if(decimals == 2){
                System.out.printf("%.2f ", vector[i]); // n decimals
            } else {
                System.out.printf("%.4f ", vector[i]); // n decimals
            }
        }
        if (blankLine) {
            System.out.println(NEW_LINE);
        }
    }

    public static void showMatrix(final double[][] matrix, int numRows, final int decimals) {
        int ct = 0;
        if (numRows == -1) {
            numRows = Integer.MAX_VALUE; // if numRows == -1, show all rows
        }
        for (int i = 0; i < matrix.length && ct < numRows; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] >= 0) {
                    System.out.print(SPACE);// blank space instead of '+' sign
                }
                System.out.printf("%." + decimals + SPACE, matrix[i][j]);
            }
            System.out.println();
            ++ct;
        }
        System.out.println();
    }
}
