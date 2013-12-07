package ua.edu.cdu.appliedMathematics.dm.newtonRaphson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class NewtonRaphson {
    public static final int INT_TRUE = 1;
    public static final int INT_FALSE = 0;
    public static final String SPACE = " ";
    public static final String MALE_SEX = "M";
    public static final String NEW_LINE = "\n";
    public static final String FEMALE_SEX = "F";
    public static final String TAB_SEPARATOR = "\t";
    public static final double FIFTY_PERCENT = 0.5;
    public static final double MALE_SEX_DOUBLE = 0.0;
    public static final double FEMALE_SEX_DOUBLE = 1.0;
    public static final double MIN_CHOLESTEROL_VALUE = 0.1;
    public static final double MAX_CHOLESTEROL_VALUE = 9.9;
    public static final DecimalFormat df = new DecimalFormat("#.##");
    public static final String TITLES_LINE = "Age" + TAB_SEPARATOR + "Sex" + TAB_SEPARATOR + "Cholesterol" + TAB_SEPARATOR + "Died";

    /**
     * generate rew data
     * @param numLines - count of lines with data
     * @param seed for Random
     * @param fileName - name of file for output data
     * @throws IOException
     */
    public static void makeRawDataFile(final int numLines, final int seed, final String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            path = Files.createFile(path);
        }
        final BufferedWriter bufferedWriter = Files.newBufferedWriter(path, Charset.defaultCharset());
        final double[] bValues = new double[]{-95.0, 0.4, -0.9, 11.2}; // hard-coded
        final Random rand = new Random(seed);

        for (int i = 0; i < numLines; ++i) {
            final int age = rand.nextInt(20) * 2 + 41; // [35,81) == [35,80]
            final String sex = rand.nextDouble() < FIFTY_PERCENT ? MALE_SEX : FEMALE_SEX;
            double cholesterol = age / 10.0 - 1.0;

            if (rand.nextInt(2) == 0){
                cholesterol = cholesterol + (3.0 - 1.0) * rand.nextDouble() + 1.0;
            } else {
                cholesterol = cholesterol - (3.0 - 1.0) * rand.nextDouble() + 1.0;
            }
            if (cholesterol > MAX_CHOLESTEROL_VALUE){
                cholesterol = MAX_CHOLESTEROL_VALUE;
            }
            if (cholesterol < MIN_CHOLESTEROL_VALUE){
                cholesterol = MIN_CHOLESTEROL_VALUE;
            }

            final double x0 = 1.0;
            final double x2 = MALE_SEX.equals(sex) ? MALE_SEX_DOUBLE : FEMALE_SEX_DOUBLE;
            final double z = (bValues[0] * x0) + (bValues[1] * age) + (bValues[2] * x2) + (bValues[3] * cholesterol);
            final double p = 1.0 / (1.0 + Math.exp(-z));

            final int die = p < FIFTY_PERCENT ? INT_FALSE : INT_TRUE;
            bufferedWriter.write(age + TAB_SEPARATOR + sex + TAB_SEPARATOR + cholesterol + TAB_SEPARATOR + die + NEW_LINE);
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * print raw data from file into console
     * @param fileName - name of file with data
     * @param steps - max count of elements printed in console
     * @throws IOException
     */
    public static void displayRawData(final String fileName, int steps) throws IOException {
        final Path path = Paths.get(fileName);
        if(Files.exists(path)){
            System.out.println(TITLES_LINE);
            System.out.println("==========================================");
            final List<String> list = Files.readAllLines(path, Charset.defaultCharset());
            if(null != list){
                for (int i = 0; i < list.size() && i < steps; i++){
                    System.out.println(list.get(i));
                }
                System.out.println(" . . . ");
            }
        }
    }

    //============================================================================================

    /**
     * read a file of raw age-sex-cholesterol-death (e.g., "68 M 7.56 1") and
     * add an initial column of all 1.0s to correspond to the B0 constant
     * the dependent Y variable (death) is loaded by a separate routine
     * @param rawDataFile - name of file for raw data
     * @return matrix with values
     * @throws IOException
     */
    public static double[][] loadRawDataIntoDesignMatrix(final String rawDataFile) throws IOException {
        final List<String> stringList = Files.readAllLines(Paths.get(rawDataFile), Charset.defaultCharset());
        final int lineCount = stringList.size();
        final double[][] result = new double[lineCount][4]; // design: col age, sex, cholesterol
        for(int i = 0; i < stringList.size(); i++){
            final String[] words = stringList.get(i).trim().split(TAB_SEPARATOR);
            result[i][0] = 1.0;
            result[i][1] = Double.parseDouble(words[0]); //age
            result[i][2] = MALE_SEX.equals(words[1]) ? MALE_SEX_DOUBLE : FEMALE_SEX_DOUBLE; //sex
            result[i][3] = Double.parseDouble(words[2]); //cholesterol
        }
        return result;
    }

    /**
     * read a file of raw age-sex-cholesterol-death (68 M 7.56 1) into a y column vector (1.0 0.0 0.0 1.0 etc)
     * @param rawDataFile - name of file for raw data
     * @return vector with values
     */
    public static double[] loadRawDataIntoYVector(final String rawDataFile) throws IOException {
        final List<String> stringList = Files.readAllLines(Paths.get(rawDataFile), Charset.defaultCharset());
        final int lineCount = stringList.size();
        final double[] result = new double[lineCount]; // single column vector for died
        for(int i = 0; i < stringList.size(); i++){
            final String[] words = stringList.get(i).trim().split(TAB_SEPARATOR);
            result[i] = Double.parseDouble(words[3]);
        }
        return result;
    }

    /**
     * returns the percent (as 0.00 to 100.00) accuracy of the bVector measured by how many lines of data are correctly predicted.
     * note: this is not the same as accuracy as measured by sum of squared deviations between
     * the probabilities produceed by bVector and 0.0 and 1.0 data in yVector
     * For predictions we simply see if the p produced by b are >= 0.50 or not.
     * @param xMatrix - matrix of coefficients
     * @param yVector - vector of free members
     * @param bVector - vector of free members
     * @return predictive accuracy coefficient
     */
    public static double predictiveAccuracy(final double[][] xMatrix, final double[] yVector, final double[] bVector) {
        final int xRows = xMatrix.length;
        final int xCols = xMatrix[0].length;
        final int yRows = yVector.length;
        final int bRows = bVector.length;
        if (xCols != bRows || xRows != yRows){
            throw new RuntimeException("Bad dimensions for xMatrix or yVector or bVector in PredictiveAccuracy()");
        }
        final double[] pVector = constructProbVector(xMatrix, bVector); // helper also used by LogisticRegressionNewtonParameters()
        int numberCasesCorrect = 0;
        int numberCasesWrong = 0;
        final int pRows = pVector.length;
        if (pRows != xRows){
            throw new RuntimeException("Unequal rows in prob vector and design matrix in PredictiveAccuracy()");
        }

        for (int i = 0; i < yRows; ++i){ // each dependent variable
            if (pVector[i] >= 0.50 && yVector[i] == 1.0){
                ++numberCasesCorrect;
            } else if (pVector[i] < 0.50 && yVector[i] == 0.0){
                ++numberCasesCorrect;
            } else {
                ++numberCasesWrong;
            }
        }

        final int total = numberCasesCorrect + numberCasesWrong;
        if (total == 0){
            return total;
        } else {
            return (100.0 * numberCasesCorrect) / total;
        }
    }

    // ============================================================================================

    /**
     * Use the Newton-Raphson technique to estimate logistic regression beta parameters
     * xMatrix is a design matrix of predictor variables where the first column is augmented with all 1.0 to represent dummy x values for the b0 constant
     * yVector is a column vector of binary (0.0 or 1.0) dependent variables
     * maxIterations is the maximum number of times to iterate in the algorithm. A value of 1000 is reasonable.
     * epsilon is a closeness parameter: if all new b[i] values after an iteration are within epsilon of
     * the old b[i] values, we assume the algorithm has converged and we return. A value like 0.001 is often reasonable.
     * jumpFactor stops the algorithm if any new beta value is jumpFactor times greater than the old value. A value of 1000.0 seems reasonable.
     * The return is a column vector of the beta estimates: b[0] is the constant, b[1] for x1, etc.
     * There is a lot that can go wrong here. The algorithm involves finding a matrx inverse (see matrixInverse) which will throw
     * if the inverse cannot be computed. The Newton-Raphson algorithm can generate beta values that tend towards infinity.
     * If anything bad happens the return is the best beta values known at the time (which could be all 0.0 values but not null).
     * @param xMatrix - matrix of coefficients
     * @param yVector - vector of free members
     * @param maxIterations - max count of iterations
     * @param epsilon - accuracy
     * @param jumpFactor - value for verify changing of another value
     * @return - vector of best beta data
     */
    public static double[] computeBestBeta(final double[][] xMatrix, final double[] yVector, 
                                    final int maxIterations, final double epsilon, final double jumpFactor) {
        final int xRows = xMatrix.length;
        final int xCols = xMatrix[0].length;
        if (xRows != yVector.length){
            throw new RuntimeException("The xMatrix and yVector are not compatible in LogisticRegressionNewtonParameters()");
        }
        // initial beta values
        double[] bVector = new double[xCols];
        for (int i = 0; i < xCols; ++i) {
            bVector[i] = 0;
        } // initialize to 0
        // best beta values found so far
        double[] bestBvector = vectorDuplicate(bVector);
        // a column vector of the probabilities of each row using the b[i] values and the x[i] values.
        double[] pVector = constructProbVector(xMatrix, bVector);
        double mse = meanSquaredError(pVector, yVector);
        int timesWorse = 0; // how many times are the new betas worse (i.e., give worse MSE) than the current betas

        for (int i = 0; i < maxIterations; ++i) {
            // generate new beta values using Newton-Raphson. could return null.
            double[] newBVector = constructNewBetaVector(bVector, xMatrix, yVector, pVector); 
            if (null == newBVector) {
                return bestBvector;
            }
            // no significant change?
            if (noChange(bVector, newBVector, epsilon)){        // we are done because of no significant change in beta[]
                return bestBvector;
            }
            // spinning out of control?
            if (outOfControl(bVector, newBVector, jumpFactor)){ // any new beta more than jumpFactor times greater than old?
                return bestBvector;
            }
            pVector = constructProbVector(xMatrix, newBVector);
            // are we getting worse or better?
            double newMSE = meanSquaredError(pVector, yVector); // smaller is better
            if (newMSE > mse){                                  // new MSE is worse than current SSD
                ++timesWorse;                                   // update counter
                if (timesWorse >= 4) {
                    return bestBvector;
                }
                bVector = vectorDuplicate(newBVector);   // update current b: old b becomes not the new b but halfway between new and old
                for (int k = 0; k < bVector.length; ++k) {
                    bVector[k] = (bVector[k] + newBVector[k]) / 2.0;
                }
                mse = newMSE;                                   // update current SSD (do not update best b because we don't have a new best b)
            } else {                                            // new SSD is be better than old
                bVector = vectorDuplicate(newBVector);          // update current b: old b becomes new b
                bestBvector = vectorDuplicate(bVector);         // update best b
                mse = newMSE;                                   // update current MSE
                timesWorse = 0;                                 // reset counter
            }
        } // end main iteration loop
        return bestBvector;

    }

    // --------------------------------------------------------------------------------------------

    /**
     * this is the heart of the Newton-Raphson technique
     * b[t] = b[t-1] + inv(X'W[t-1]X)X'(y - p[t-1])
     *
     * b[t] is the new (time t) b column vector
     * b[t-1] is the old (time t-1) vector
     * X' is the transpose of the X matrix of x data (1.0, age, sex, chol)
     * W[t-1] is the old weight matrix
     * y is the column vector of binary dependent variable data
     * p[t-1] is the old column probability vector (computed as 1.0 / (1.0 + exp(-z) where z = b0x0 + b1x1 + . . .)

     * note: W[t-1] is nxn which could be huge so instead of computing b[t] = b[t-1] + inv(X'W[t-1]X)X'(y - p[t-1])
     * compute b[t] = b[t-1] + inv(X'X~)X'(y - p[t-1]) where X~ is W[t-1]X computed directly
     * the idea is that the vast majority of W[t-1] cells are 0.0 and so can be ignored
     * @param oldBetaVector - old beta vector
     * @param xMatrix - matrix of coefficients
     * @param yVector - vector of free members
     * @param oldProbVector - old vector
     * @return - new beta vector
     */
    public static double[] constructNewBetaVector(final double[] oldBetaVector, final double[][] xMatrix, 
                                                  final double[] yVector, final double[] oldProbVector) {
        final double[][] Xt = matrixTranspose(xMatrix);                 // X'
        final double[][] A = computeXTilde(oldProbVector, xMatrix);     // WX
        final double[][] B = matrixProduct(Xt, A);                      // X'WX

        final double[][] inverse = matrixInverse(B);                    // inv(X'WX)
        if (null == inverse){                                           // computing the inverse can blow up easily
            return null;
        }
        final double[][] D = matrixProduct(inverse, Xt);                // inv(X'WX)X'
        final double[] YP = vectorSubtraction(yVector, oldProbVector);  // y-p
        final double[] E = matrixVectorProduct(D, YP);                  // inv(X'WX)X'(y-p)
        return vectorAddition(oldBetaVector, E);                        // b + inv(X'WX)X'(y-p) (could be null!)
    }

    // --------------------------------------------------------------------------------------------

    /**
     * note: W[t-1] is nxn which could be huge so instead of computing b[t] = b[t-1] + inv(X'W[t-1]X)X'(y - p[t-1]) directly
     * we compute the W[t-1]X part, without the use of W.
     * Since W is derived from the prob vector and W has non-0.0 elements only on the diagonal we can avoid a ton of work
     * by using the prob vector directly and not computing W at all.
     * Some of the research papers refer to the product W[t-1]X as X~ hence the name of this method.
     * ex: if xMatrix is 10x4 then W would be 10x10 so WX would be 10x4 -- the same size as X
     * @param pVector - vector vith data
     * @param xMatrix - matrix of coefficients
     * @return new matrix
     */
    public static double[][] computeXTilde(final double[] pVector, final double[][] xMatrix) {
        final int pRows = pVector.length;
        final int xRows = xMatrix.length;
        final int xCols = xMatrix[0].length;

        if (pRows != xRows){
            throw new RuntimeException("The pVector and xMatrix are not compatible in computeXTilde");
        }
        // we are not doing marix multiplication. the p column vector sort of lays on top of each matrix column.
        final double[][] result = new double[pRows][xCols];

        for (int i = 0; i < pRows; ++i) {
            for (int j = 0; j < xCols; ++j) {
                result[i][j] = pVector[i] * (1.0 - pVector[i]) * xMatrix[i][j]; // note the p(1-p)
            }
        }
        return result;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * true if all new b values have changed by amount smaller than epsilon
     * @param oldBVector - old vector with data
     * @param newBVector - new vector with data
     * @param epsilon - accuracy
     * @return true if !oldVector.equals(newVector) with accuracy = epsilon
     */
    public static boolean noChange(final double[] oldBVector, final double[] newBVector, final double epsilon) {
        for (int i = 0; i < oldBVector.length; ++i) {
            if (Math.abs(oldBVector[i] - newBVector[i]) > epsilon){ // we have at least one change
                return false;
            }
        }
        return true;
    }

    /**
     * true if any new b is jumpFactor times greater than old b
     * @param oldBVector - old data
     * @param newBVector - new data
     * @param jumpFactor - value for verify changing of another value
     * @return boolean true or false
     */
    public static boolean outOfControl(final double[] oldBVector, final double[] newBVector, final double jumpFactor) {
        for (int i = 0; i < oldBVector.length; ++i) {
            if (oldBVector[i] == 0.0){
                return false; // if old is 0.0 anything goes for the new value
            }
            if (Math.abs(oldBVector[i] - newBVector[i]) / Math.abs(oldBVector[i]) > jumpFactor){ // too big a change.
                return true;
            }
        }
        return false;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * p = 1 / (1 + exp(-z) where z = b0x0 + b1x1 + b2x2 + b3x3 + . . .
     * suppose X is 10 x 4 (cols are: x0 = const. 1.0, x1, x2, x3)
     * then b would be a 4 x 1 (col vecror)
     * then result of X times b is (10x4)(4x1) = (10x1) column vector
     * @param xMatrix - matrix of coefficients
     * @param bVector - vector
     * @return prob vector
     */
    public static double[] constructProbVector(final double[][] xMatrix, final double[] bVector) {
        final int xRows = xMatrix.length;
        final int xCols = xMatrix[0].length;
        final int bRows = bVector.length;
        if (xCols != bRows){
            throw new RuntimeException("xMatrix and bVector are not compatible in constructProbVector");
        }
        // ex: if xMatrix is size 10 x 4 and bVector is 4 x 1 then prob vector is 10 x 1 (one prob for every row of xMatrix)
        final double[] result = new double[xRows];
        for (int i = 0; i < xRows; ++i) {
            double z = 0.0;
            for (int j = 0; j < xCols; ++j) {
                z += xMatrix[i][j] * bVector[j]; // b0(1.0) + b1x1 + b2x2 + . . .
            }
            // consider checking for huge value of Math.Exp(-z) here;
            result[i] = 1.0 / (1.0 + Math.exp(-z));
        }
        return result;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * how good are the predictions? (using an already-calculated prob vector)
     * note: it is possible that a model with better (lower) MSE than a second model could give worse predictive accuracy.
     * @param pVector - first vector
     * @param yVector - second vector
     * @return squared error
     */
    public static double meanSquaredError(final double[] pVector, final double[] yVector) {
        final int pRows = pVector.length;
        final int yRows = yVector.length;
        if (pRows != yRows){
            throw new RuntimeException("The prob vector and the y vector are not compatible in meanSquaredError()");
        }
        if (pRows == 0){
            return 0;
        }
        double result = 0;
        for (int i = 0; i < pRows; ++i) {
            result += (pVector[i] - yVector[i]) * (pVector[i] - yVector[i]);
            //result += Math.abs(pVector[i] - yVector[i]); // average absolute deviation approach
        }
        return result / pRows;
    }

    // ============================================================================================

    /**
     * represent numRows rows of matrix as string
     * @param matrix - matrix with values
     * @param numRows - count of rows for print
     * @return string representation of matrix
     */
    public static String matrixAsString(final double[][] matrix, final int numRows) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length && i < numRows; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                sb.append(matrix[i][j]).append(SPACE);
            }
            sb.append(NEW_LINE);
        }
        return sb.toString();
    }

    /**
     * allocates/creates a duplicate of a matrix. assumes matrix is not null.
     * @param matrix - matrix with values
     * @return copy of matrix
     */
    public static double[][] matrixDuplicate(final double[][] matrix) {
        final double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; ++i){ // copy the values
            System.arraycopy(matrix[i], 0, result[i], 0, matrix[i].length);
        }
        return result;
    }

    /**
     * add two vectors
     * @param vectorA - first vector
     * @param vectorB - second vector
     * @return sum of vectors
     */
    public static double[] vectorAddition(final double[] vectorA, final double[] vectorB) {
        final int aRows = vectorA.length;
        final int bRows = vectorB.length;
        if (aRows != bRows){
            throw new RuntimeException("Non-conformable vectors in vectorAddition");
        }
        final double[] result = new double[aRows];
        for (int i = 0; i < aRows; ++i){
            result[i] = vectorA[i] + vectorB[i];
        }
        return result;
    }

    /**
     * subtraction of two vector
     * @param vectorA - first vector
     * @param vectorB - second vector
     * @return difference of vectors
     */
    public static double[] vectorSubtraction(final double[] vectorA, final double[] vectorB) {
        final int aRows = vectorA.length;
        final int bRows = vectorB.length;
        if (aRows != bRows){
            throw new RuntimeException("Non-conformable vectors in vectorSubtraction");
        }
        final double[] result = new double[aRows];
        for (int i = 0; i < aRows; ++i){
            result[i] = vectorA[i] - vectorB[i];
        }
        return result;
    }

    /**
     * represent numRows rows of vector as string
     * @param vector - vector with values
     * @param count - count of rows for print
     * @return string representation of vector
     */
    public static String vectorAsString(final double[] vector, final int count) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vector.length && i < count; ++i){
            sb.append(df.format(vector[i])).append(SPACE);
        }
        sb.append(NEW_LINE);
        return sb.toString();
    }

    /**
     * copy vector
     * @param vector - vector with values
     * @return copy of vector
     */
    public static double[] vectorDuplicate(final double[] vector) {
        final double[] result = new double[vector.length];
        System.arraycopy(vector, 0, result, 0, vector.length);
        return result;
    }

    /**
     * transpose matrix
     * @param matrix matrix with values
     * @return transposed matrix
     */
    public static double[][] matrixTranspose(final double[][] matrix){
        final int rows = matrix.length;
        final int cols = matrix[0].length; // assume all columns have equal size
        final double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    /**
     * product matrix on matrix
     * @param matrixA - first matrix
     * @param matrixB - second matrix
     * @return multiplication of two matrix
     */
    public static double[][] matrixProduct(final double[][] matrixA, final double[][] matrixB) {
        final int aRows = matrixA.length;
        final int aCols = matrixA[0].length;
        final int bRows = matrixB.length;
        final int bCols = matrixB[0].length;
        if (aCols != bRows){
            throw new RuntimeException("Non-conformable matrices in matrixProduct");
        }
        final double[][] result = new double[aRows][bCols];
        for (int i = 0; i < aRows; ++i){ // each row of A
            for (int j = 0; j < bCols; ++j){ // each col of B
                for (int k = 0; k < aCols; ++k){ // could use k < bRows
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    /**
     * multiply matrix on vector
     * @param matrix - matrix for product
     * @param vector - vector
     * @return result of product matrix on vector
     */
    public static double[] matrixVectorProduct(final double[][] matrix, final double[] vector) {
        final int mRows = matrix.length;
        final int mCols = matrix[0].length;
        final int vRows = vector.length;
        if (mCols != vRows){
            throw new RuntimeException("Non-conformable matrix and vector in matrixVectorProduct");
        }
        final double[] result = new double[mRows]; // an n x m matrix times a m x 1 column vector is a n x 1 column vector
        for (int i = 0; i < mRows; ++i){
            for (int j = 0; j < mCols; ++j){
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    /**
     * inverse matrix
     * @param matrix - matrix for inverse
     * @return inverse matrix
     */
    public static double[][] matrixInverse(final double[][] matrix) {
        final int n = matrix.length;
        final double[][] result = matrixDuplicate(matrix);

        final int[] perm = new int[n];
        final double[][] lum = matrixDecompose(matrix, perm);
        if (null == lum){
            return null;
        }
        final double[] b = new double[n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == perm[j]){
                    b[j] = 1.0;
                } else {
                    b[j] = 0.0;
                }
            }
            double[] x = helperSolve(lum, b);
            for (int j = 0; j < n; ++j){
                result[j][i] = x[j];
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------------------------------------------------

    /**
     * solve Ax = b if you already have luMatrix from A and b has been permuted
     * @param luMatrix - matrix of coefficients
     * @param b - vector of free members
     * @return vector of answers
     */
    public static double[] helperSolve(final double[][] luMatrix, final double[] b) {
        int n = luMatrix.length;
        // solve Ly = b using forward substitution
        for (int i = 1; i < n; ++i) {
            double sum = b[i];
            for (int j = 0; j < i; ++j) {
                sum -= luMatrix[i][j] * b[j];
            }
            b[i] = sum;
        }
        // solve Ux = y using backward substitution
        b[n - 1] /= luMatrix[n - 1][n - 1];
        for (int i = n - 2; i >= 0; --i) {
            double sum = b[i];
            for (int j = i + 1; j < n; ++j) {
                sum -= luMatrix[i][j] * b[j];
            }
            b[i] = sum / luMatrix[i][i];
        }
        return b;
    }

    // -------------------------------------------------------------------------------------------------------------------

    /**
     * Doolittle's method (1.0s on L diagonal) with partial pivoting
     * @param matrix - input matrix
     * @param perm - some vector
     * @return decomposed matrix
     */
    static double[][] matrixDecompose(final double[][] matrix, int[] perm) {
        int rows = matrix.length;
        int cols = matrix[0].length; // assume all rows have the same number of columns so just use row [0].
        if (rows != cols){
            throw new RuntimeException("Attempt to matrixDecompose a non-square mattrix");
        }
        final double[][] result = matrixDuplicate(matrix); // make a copy of the input matrix
        for (int i = 0; i < rows; ++i) {
            perm[i] = i;
        }
        int tog = 1; // toggle tracks number of row swaps. used by MatrixDeterminant

        double ajj, aij;

        for (int j = 0; j < rows - 1; ++j){ // each column
            double max = Math.abs(result[j][j]); // find largest value in row
            int pRow = j;
            for (int i = j + 1; i < rows; ++i) {
                aij = Math.abs(result[i][j]);
                if (aij > max) {
                    max = aij;
                    pRow = i;
                }
            }

            if (pRow != j){ // if largest value not on pivot, swap rows
                double[] rowPtr = result[pRow];
                result[pRow] = result[j];
                result[j] = rowPtr;

                int tmp = perm[pRow]; // and swap perm info
                perm[pRow] = perm[j];
                perm[j] = tmp;

                tog = -tog; // adjust the row-swap toggle
            }

            ajj = result[j][j];
            if (Math.abs(ajj) < 0.00000001){ // if diagonal after swap is zero . . .
                return null; // consider a throw
            }
            for (int i = j + 1; i < rows; ++i) {
                aij = result[i][j] / ajj;
                result[i][j] = aij;
                for (int k = j + 1; k < rows; ++k) {
                    result[i][k] -= aij * result[j][k];
                }
            }
        }
        return result;
    }
}