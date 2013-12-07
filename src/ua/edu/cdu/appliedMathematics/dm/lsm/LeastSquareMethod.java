package ua.edu.cdu.appliedMathematics.dm.lsm;

public class LeastSquareMethod {

    /**
     * Least Square Method for general function
     * @param x - free members
     * @param y - dependent members
     * @return vector of coefficients of unknown function
     */
    public static double[] lsmGeneral(final double[] x, final double[] y, final int powOfFunction){
        if(null == x || null == y || x.length != y.length){
            return null;
        }
        final int length = x.length;
        //make new vector with values 0
        final double[] xs = makeVector(2 * powOfFunction + 1, 0);
        //make new vector with values 0
        final double[] xys = makeVector(powOfFunction + 1, 0);
        //make new matrix ((powOfFunction+1)x(powOfFunction+1)) with values 0
        final double[][] matrix = makeMatrix(powOfFunction + 1, powOfFunction + 1, 0);
        //make new vector with values 0
        final double[] vector = makeVector(powOfFunction + 1, 0);
        xs[0] = length-1;
        //calculate the group mean of x
        for(int j = 2*powOfFunction; j >= 1; j--){
            for (final double aX : x) {
                xs[j] = xs[j] + Math.pow(aX, j);
            }
        }
        //calculate the group mean of x*y
        for(int j = powOfFunction; j >= 0; j--){
            for(int i = 0; i < length; i++){
                xys[j] = xys[j] + y[i]*Math.pow(x[i], j);
            }
        }
        //generate matrix of coefficients of equations
        for(int j = 0; j < powOfFunction + 1; j++){
            for(int i = 0; i < powOfFunction + 1; i++){
                matrix[j][i] = xs[2*powOfFunction-i-j];
            }
        }
        //generate vector of free members
        for(int i = powOfFunction; i >= 0; i--){
            vector[powOfFunction-i] = xys[i];
        }
        //solve system of equations
        return gaussMethod(matrix, vector);
    }

    /**
     * Gauss method for solving equation
     * @param a - matrix of coefficients of equation
     * @param b - vector of free members
     * @return vector of solutions
     */
    public static double[] gaussMethod(double[][] a, double[] b){
        if(null == a || null == b){
            return null;
        }
        final int length = a[0].length;
        int[] indexes = makeIndexesVector(length);
        for(int i = 0; i < length-1; i++){
            double max = Math.abs(a[indexes[i]][i]);
            int imax = i;
            for (int j = i+1; j < length; j++){
                if(Math.abs(a[indexes[j]][i]) > max){
                    max = Math.abs(a[indexes[j]][i]);
                    imax = j;
                }
            }
            if(imax != i){
                int tmp = indexes[i];
                indexes[i] = indexes[imax];
                indexes[imax] = tmp;
            }
            for(int j = i+1; j < length; j++){
                double c = -a[indexes[j]][i]/a[indexes[i]][i];
                for(int powOfFunction = i; powOfFunction < length; powOfFunction++){
                    a[indexes[j]][powOfFunction] = a[indexes[j]][powOfFunction]+c*a[indexes[i]][powOfFunction];
                }
                b[indexes[j]] = b[indexes[j]] + c*b[indexes[i]];
            }
        }
        double x[] = new double[length];
        x[length-1] = b[indexes[length-1]]/a[indexes[length-1]][length-1];
        for(int i = length-2; i >= 0; i--){
            for(int j = i+1; j < length; j++){
                b[indexes[i]] = b[indexes[i]] - a[indexes[i]][j]*x[j];
            }
            x[i] = b[indexes[i]]/a[indexes[i]][i];
        }
        return x;
    }

    /**
     * Least Square Method for linear function
     * @param x - free members
     * @param y - dependent members
     * @return vector of coefficients of unknown function
     */
    public static double[] simpleLSM(final double[] x, final double[] y){
        if(null == x || null == y || x.length != y.length){
            return null;
        }
        final int length = x.length;
        double xk = 0, xx = 0, yy = 0, xy = 0;
        //generate coefficients of system of linear equation
        for(int i = 0; i < length; i++){
            //calculate the group mean x^2
            xk += x[i]*x[i];
            //calculate the group mean of x
            xx += x[i];
            //calculate the group mean of y
            yy += y[i];
            //calculate the group mean of x*y
            xy += x[i]*y[i];
        }
        //count determinants
        final double d = determinant(new double[][]{{xk, xx}, {xx, length}});
        final double d1 = determinant(new double[][]{{xy, yy}, {xx, length}});
        final double d2 = determinant(new double[][]{{xk, xx}, {xy, yy}});
        //find solution of equation
        final double a = d1/d;
        final double b = d2/d;
        //return solution as vector
        return new double[]{a, b};
    }

    /**
     * count determinant of matrix
     * @param doubles - input matrix
     * @return - determinant
     */
    public static double determinant(final double[][] doubles) {
        final int length = doubles[0].length;
        double d = 0;
        double determinant = 1;
        final int[] indexes = makeIndexesVector(length);
        for(int i = 0; i < length-1; i++){
            double max = Math.abs(doubles[indexes[i]][i]);
            int imax = i;
            for(int j = i+1; j < length; j++){
                if(Math.abs(doubles[indexes[j]][i]) > max){
                    max = Math.abs(doubles[indexes[j]][i]);
                    imax = j;
                }
            }
            if(imax != i){
                int tmp = indexes[i];
                indexes[i] = indexes[imax];
                indexes[imax] = tmp;
                d = d + 1;
            }
            for(int j = i+1; j < length; j++){
                double c = -doubles[indexes[j]][i]/doubles[indexes[i]][i];
                for(int l = i; l < length; l++){
                    doubles[indexes[j]][l] = doubles[indexes[j]][l]+c*doubles[indexes[i]][l];
                }
            }
        }
        for(int i = 0; i < length; i++){
            determinant = determinant * doubles[indexes[i]][i];
        }
        return Math.pow(-1, d) * determinant;
    }

    /**
     * make vector with indexes
     * @param length - length of vector
     * @return new vector
     */
    private static int[] makeIndexesVector(final int length) {
        final int[] array = new int[length];
        for(int i = 0; i < length; i++){
            array[i] = i;
        }
        return array;
    }

    /**
     * make vector with length = length and values = value
     * @param length - number of elements in vector
     * @param value - value of each elements in vector
     * @return new vector
     */
    private static double[] makeVector(final int length, final double value) {
        final double[] array = new double[length];
        for(int i = 0; i < length; i++){
            array[i] = value;
        }
        return array;
    }

    /**
     * make matrix rows*columns with values = value
     * @param rows - number of rows in matrix
     * @param columns - number of columns in matrix
     * @param value - value of each elements in matrix
     * @return new matrix
     */
    private static double[][] makeMatrix(final int rows, final int columns, final double value) {
        final double[][] array = new double[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                array[i][j] = value;
            }
        }
        return array;
    }
}
