package ua.edu.cdu.appliedMathematics.dm.newtonRaphson;

import java.io.IOException;

public class Main {
    
    /**
     * Demo logistic regression using Newton-Raphson
     * Note: I left in, but commenteed out, a lot of code in that I used for debugging.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Begin Logistic Regression with Newton-Raphson demo");

        String trainFile = "trainFile.txt";  // will be placed in same directory as executable
        String testFile = "testFile.txt";

        System.out.println("Creating 80 lines of synthetic training data and 20 lines of test data");
        NewtonRaphson.makeRawDataFile(80, 3, trainFile);
        NewtonRaphson.makeRawDataFile(20, 4, testFile);

        System.out.println("First 5 lines of training data file are: ");
        NewtonRaphson.displayRawData(trainFile, 5);

        System.out.println("Loading train and test data from files into memory");

        double[][] xTrainMatrix = NewtonRaphson.loadRawDataIntoDesignMatrix(trainFile);
        System.out.println("First five rows of x training matrix:");
        System.out.println(NewtonRaphson.matrixAsString(xTrainMatrix, 5));

        double[] yTrainVector = NewtonRaphson.loadRawDataIntoYVector(trainFile);
        System.out.println("First five rows of y training vector:");
        System.out.println(NewtonRaphson.vectorAsString(yTrainVector, 5));

        double[][] xTestMatrix = NewtonRaphson.loadRawDataIntoDesignMatrix(testFile);
        double[] yTestVector = NewtonRaphson.loadRawDataIntoYVector(testFile);

        System.out.println("Setting Newton-Raphson algorithm stop conditions");
        int maxIterations = 25;
        double epsilon = 0.01; // stop if all new beta values change less than epsilon (algorithm has converged?)
        double jumpFactor = 1000.0; // stop if any new beta jumps too much (algorithm spinning out of control?)
        System.out.println("maxIterations = " + maxIterations + "  epsilon = " + epsilon + "  jumpFactor = " + jumpFactor);

        System.out.println("Using Newton-Raphson to find beta parameters that best fit the training data");
        // computing the beta parameters is synonymous with 'training'
        double[] beta = NewtonRaphson.computeBestBeta(xTrainMatrix, yTrainVector, maxIterations, epsilon, jumpFactor);
        System.out.println("Newton-Raphson complete");
        System.out.println("The beta vector is: ");
        System.out.println(NewtonRaphson.vectorAsString(beta, Integer.MAX_VALUE));

        System.out.println("Computing accuracy on test data using the beta values");
        // percent of data cases correctly predicted in the test data set.
        double acc = NewtonRaphson.predictiveAccuracy(xTestMatrix, yTestVector, beta);
        System.out.println("The predictive accuracy of the model on the test data is " + acc);

        System.out.println("End demo");
    }
}
