package ua.edu.cdu.appliedMathematics.dm.neuralNetwork.utils;

public class OperationsWithMatrixAndVectors {

    public static final String VALUES_SEPARATOR = "\t";

    /**
     * creates replicatedMatrix large matrix B consisting of an rowTimesNumber-by-columnTimesNumber tiling of copies of array.
     * @param array - original matrix
     * @param rowTimesNumber - count of row copies
     * @param columnTimesNumber - count of columns copies
     * @return new matrix original.rows*rowTimesNumber-by-original.cols*columnTimesNumber
     */
    public static double[][] replicateMatrix(final double[][] array, final int rowTimesNumber, final int columnTimesNumber){
        if(null == array || rowTimesNumber < 1 || columnTimesNumber < 1){
            return array;
        }
        final int replicatedMatrixRows = array.length * rowTimesNumber;
        final int replicatedMatrixColumns = array[0].length * columnTimesNumber;
        final double[][] replicatedMatrix = new double[replicatedMatrixRows][replicatedMatrixColumns];
        for (int i = 0; i < replicatedMatrixRows; i++) {
            for (int j = 0; j < replicatedMatrixColumns; j++) {
                replicatedMatrix[i][j] = array[i%array.length][j%array[0].length];
            }
        }
        return replicatedMatrix;
    }

    /**
     * creates replicatedMatrix large matrix B consisting of an rowTimesNumber-by-replicatedMatrixColumns tiling of copies of array.
     * @param array - original vector
     * @param rowTimesNumber - count of row copies
     * @param replicatedMatrixColumns - count of columns copies
     * @return new matrix original.rows*rowTimesNumber-by-replicatedMatrixColumns
     */
    public static double[][] replicateMatrix(final double[] array, final int rowTimesNumber, final int replicatedMatrixColumns){
        if(null == array || rowTimesNumber < 1 || replicatedMatrixColumns < 1){
            return null;
        }
        final int replicatedMatrixRows = array.length * rowTimesNumber;
        final double[][] replicatedMatrix = new double[replicatedMatrixRows][replicatedMatrixColumns];
        for (int i = 0; i < replicatedMatrixRows; i++) {
            for (int j = 0; j < replicatedMatrixColumns; j++) {
                replicatedMatrix[i][j] = array[i%array.length];
            }
        }
        return replicatedMatrix;
    }

    /**
     * print matrix @param array
     */
    public static void showArray(final double[][] array) {
        if(null == array){
            System.out.println("Array is null!!!");
            return;
        }
        for (double[] aMatrix : array) {
            for (int j = 0; j < array[0].length; ++j) {
                System.out.printf(aMatrix[j] + VALUES_SEPARATOR);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * print vector @param array
     */
    public static void showArray(final double[] array) {
        if(null == array){
            System.out.println("Vector is null!!!");
            return;
        }
        for (double anArray : array) {
            System.out.printf(anArray + VALUES_SEPARATOR);
        }
        System.out.println();
    }

    /**
     * print double vector
     * @param array vector for printing
     * @param elementsPerLine - elements in one line
     */
    public static void showArray(final double[] array, final int elementsPerLine) {
        if(null == array){
            System.out.println("Vector is null!!!");
            return;
        }
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if((i+1)%elementsPerLine == 0){
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * print int vector
     * @param array vector for printing
     * @param elementsPerLine - elements in one line
     */
    public static void showArray(final int[] array, final int elementsPerLine) {
        if(null == array){
            System.out.println("Vector is null!!!");
            return;
        }
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if((i+1)%elementsPerLine == 0){
                System.out.println();
            }
        }
        //System.out.println();
    }

    /**
     * count sums of all columns
     * @param array - input matrix
     * @return vector of sums of all elements in each column
     */
    public static double[] sum(final double[][] array){
        if(null == array){
            return null;
        }
        final double[] sums = new double[array[0].length];
        for (double[] anArray : array) {
            for (int j = 0; j < anArray.length; j++) {
                sums[j] += anArray[j];
            }
        }
        return sums;
    }

    /**
     * count sum of all numbers in vector
     * @param array - input vector
     * @return sum of all numbers in b=vector
     */
    public static double sum(final double[] array){
        if(null == array){
            return 0;
        }
        double sum = 0;
        for (final double aMatrix : array) {
            sum += aMatrix;
        }
        return sum;
    }

    /**
     * applies Math.abs() to each elements of matrix
     * @param array original matrix
     */
    public static double[][] absMatrix(final double[][] array) {
        if(null == array){
            return array;
        }
        final double[][] result = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = Math.abs(array[i][j]);
            }
        }
        return result;
    }

    /**
     * applies Math.abs() to each elements of vector
     * @param array original vector
     */
    public static double[] absMatrix(final double[] array){
        if(null == array){
            return array;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Math.abs(array[i]);
        }
        return result;
    }

    /**
     * applies - to each elements of matrix
     * @param array original matrix
     */
    public static double[][] minusMatrix(final double[][] array) {
        if(null == array){
            return array;
        }
        final double[][] result = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = -array[i][j];
            }
        }
        return result;
    }

    /**
     * applies - to each elements of vector
     * @param array original vector
     */
    public static double[] minusMatrix(final double[] array) {
        if(null == array){
            return array;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = -array[i];
        }
        return result;
    }

    /**
     * concatenate two double vectors
     * @param first - first vector
     * @param second - second vector
     * @return - new vector
     */
    public static double[] concat2Vectors(final double[] first, final double[] second){
        if(null == first || null == second){
            return null;
        }
        final double[] result = new double[first.length+second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * concatenate two int vectors
     * @param first - first vector
     * @param second - second vector
     * @return - new vector
     */
    public static int[] concat2Vectors(final int[] first, final int[] second){
        if(null == first || null == second){
            return null;
        }
        final int[] result = new int[first.length+second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * transform int array into double array
     * @param array input int array
     * @return new double array
     */
    public static double[][] intArrayToDouble(final int[][] array) {
        if(null == array){
            return null;
        }
        final double[][] result = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = array[i][j];
            }
        }
        return result;
    }

    /**
     * transform int array into double vector
     * @param array input int vector
     * @return new double vector
     */
    public static double[] intArrayToDouble(final int[] array) {
        if(null == array){
            return null;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * multiplies each element of a matrix by a constant
     * @param array - two-dimensional array
     * @param number - constant
     */
    public static double[][] matrixMultiplyByNumber(final double[][] array, final double number){
        if(null == array){
            return array;
        }
        final double[][] result = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = array[i][j] * number;
            }
        }
        return result;
    }

    /**
     * multiplies each element of a vector by a constant
     * @param array - vector
     * @param number - constant
     */
    public static double[] matrixMultiplyByNumber(final double[] array, final double number){
        if(null == array){
            return array;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] * number;
        }
        return result;
    }

    /**
     * plus each element of a matrix to a constant
     * @param array - two-dimensional array
     * @param number - constant
     */
    public static double[][] matrixPlusNumber(final double[][] array, final double number){
        if(null == array){
            return array;
        }
        final double[][] result = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][j] = array[i][j] + number;
            }
        }
        return result;
    }

    /**
     * plus each element of a vector to a constant
     * @param array - vector
     * @param number - constant
     */
    public static double[] matrixPlusNumber(final double[] array, final double number){
        if(null == array){
            return array;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] + number;
        }
        return result;
    }

    /**
     * of each element of the first matrix takes
     * the corresponding element of the second matrix
     * @param a - first two-dimensional array
     * @param b - second two-dimensional array
     * @return double two-dimensional array
     */
    public static double[][] matrixPlusMatrixOneByOne(final double[][] a, final double[][] b) {
        if (b != null && a != null && a.length == b.length
                && a[0].length == b[0].length) {
            final int rows = b.length;
            final int cols = b[0].length;
            final double[][] result = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = a[i][j] + b[i][j];
                }
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * of each element of the first vector takes
     * the corresponding element of the second vector
     * @param a - vector
     * @param b - vector
     * @return new double vector
     */
    public static double[] matrixPlusMatrixOneByOne(final double[] a, final double[] b) {
        if (b != null && a != null && a.length == b.length) {
            final int size = b.length;
            final double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = a[i] + b[i];
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * create new matrix and initialize all values with Math.random()
     * @param rows - rows count
     * @param cols - cols count
     * @return new random matrix
     */
    public static double[][] makeRandomMatrix(final int rows, final int cols){
        final double[][] matrix = new double[rows][cols];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }

    /**
     * create new vector and initialize all values with Math.random()
     * @param size - rows count
     * @return new random vector
     */
    public static double[] makeRandomMatrix(final int size){
        final double[] matrix = new double[size];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Math.random();
        }
        return matrix;
    }

    /**
     * each element of first matrix multiplying by element with same index from second matrix
     * @param a - first matrix
     * @param b - second matrix
     * @return new matrix
     */
    public static double[][] matrixMultiplyMatrixOneByOne(final double[][] a, final double[][] b) {
        if(null == a || null == b){
            return null;
        }
        final double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < Math.min(b.length,a.length); i++) {
            for (int j = 0; j < Math.min(b[i].length,a[i].length); j++) {
                result[i][j] = a[i][j]*b[i][j];
            }
        }
        return result;
    }

    /**
     * each element of first vector multiplying by element with same index from second vector
     * @param a - first vector
     * @param b - second vector
     * @return new vector
     */
    public static double[] matrixMultiplyMatrixOneByOne(final double[] a, final double[] b) {
        if(null == a || null == b){
            return null;
        }
        final double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i]*b[i];
        }
        return result;
    }

    /**
     * finding max value in matrix
     * @param array test matrix
     * @return maximum value
     */
    public static double max(final double[][] array) {
        if(null == array){
            return Double.MIN_VALUE;
        }
        double max = array[0][0];
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if(max < array[i][j]){
                    max = array[i][j];
                }
            }
        }
        return max;
    }

    /**
     * finding max value in vector and index of max value
     * @param array test vector
     * @return vector with index and maximum value result[0] = indexOfMax, result[1] = max
     */
    public static double[] max(final double[] array) {
        if(null == array){
            return new double[]{-1, Double.MIN_VALUE};
        }
        double max = array[0];
        int idx = 0;
        for (int i = 1; i < array.length; i++) {
            if(max < array[i]){
                max = array[i];
                idx = i;
            }
        }
        return new double[]{idx, max};
    }

    /**
     * finding index of max value in vector
     * @param array test vector
     * @return index of maximum value
     */
    public static int idxOfMax(final double[] array) {
        if(null == array){
            return -1;
        }
        double max = array[0];
        int idx = 0;
        for (int i = 1; i < array.length; i++) {
            if(max < array[i]){
                max = array[i];
                idx = i;
            }
        }
        return idx;
    }

    /**
     * gets one column of matrix as vector
     * @param array - original matrix
     * @param colNumber - index of column
     * @return one column from input matrix
     */
    public static double[] getColAsVector(final double[][] array, final int colNumber){
        if(null == array){
            return null;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i][colNumber];
        }
        return result;
    }

    /**
     * copy vector with length from start index
     * @param array - original vector
     * @param start - start index
     * @param length - length of new vector
     * @return new vector
     */
    public static double[] copyPartOfVector(final double[] array, final int start, final int length){
        if(length > array.length || start > array.length){
            return null;
        }
        final double[] result = new double[length];
        for (int i = start, j = 0; j < length; i++, j++) {
            result[j] = array[i];
        }
        return result;
    }

    /**
     * fillArray(rows, cols, number) returns an rows-by-cols matrix of number
     * @param rows - rows count
     * @param cols - cols count
     * @param number - initial value
     * @return double matrix rows-by-cols
     */
    public static double[][] createAndFillMatrix(final int rows, final int cols, final double number) {
        if(rows < 1 || cols < 1){
            return null;
        }
        final double[][] array = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = number;
            }
        }
        return array;
    }

    /**
     * create square array and fill diagonal elements
     * @param size - size of square matrix
     * @param number - initial value
     * @return new matrix
     */
    public static double[][] eye(final int size, final double number) {
        final double[][] array = new double[size][size];
        for (int i = 0; i < size; i++) {
            array[i][i] = number;
        }
        return array;
    }

    /**
     * multiply first two-dimensional array on second two-dimensional array
     * @param a - first two-dimensional array
     * @param b - second two-dimensional array
     * @return result two-dimensional array
     */
    public static double[][] matrixMultiplyMatrix(final double[][] a, final double[][] b) {
        if(null == a || null == b || a[0].length != b.length){
            return null;
        }
        final int rows = a.length;
        final int cols = b[0].length;
        final int widthAndHeight = a[0].length;
        final double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0;
                for (int k = 0; k < widthAndHeight; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * multiply first two-dimensional array on vector
     * @param a - first two-dimensional array
     * @param b - vector
     * @return result vector
     */
    public static double[] matrixMultiplyVector(final double[][] a, final double[] b) {
        if(null == a || null == b || a[0].length != b.length){
            return null;
        }
        final int rows = a.length;
        final int widthAndHeight = a[0].length;
        final double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int k = 0; k < widthAndHeight; k++) {
                sum += a[i][k] * b[k];
            }
            result[i] = sum;
        }
        return result;
    }

    /**
     * transposes the two-dimensional array
     * @param array two-dimensional array
     * @return double transposed two-dimensional array
     */
    public static double[][] transpositionOfMatrix(final double[][] array) {
        if(null == array){
            return array;
        }
        final int rows = array.length;
        final int cols = array[0].length;
        final double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = array[i][j];
            }
        }
        return result;
    }
}