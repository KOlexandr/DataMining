package ua.edu.cdu.appliedMathematics.dm.neuralNetwork;

import static ua.edu.cdu.appliedMathematics.dm.neuralNetwork.utils.OperationsWithMatrixAndVectors.*;

public class BackLearning {
    /**
     * direct step of calculating output data
    */
    public static BackLearningBean directStep(final double[] in, final double[][] weightIn, final double[][] weightOut, final int ys){
        // calc hidden layer
        double[] sums = sum(matrixMultiplyMatrixOneByOne(replicateMatrix(in, 1, 7), weightIn));
        sums = concat2Vectors(new double[]{1}, sygmoid(sums));
        // calc output layer
        final double[] out = sygmoid(sum(matrixMultiplyMatrixOneByOne(replicateMatrix(sums, 1, ys), weightOut)));
        return new BackLearningBean(out, sums);
    }

    /**
     * return vector with values of 1/(1+e^(-array[i]))
     * @param array - input vector
     * @return new vector
     */
    public static double[] sygmoid(final double[] array){
        final double[] ones = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            ones[i] = 1/(1 + Math.exp(-array[i]));
        }
        return ones;
    }

    /**
     * we have 3 layers : input-> hidden ->output
     * and we have some input data and result, and we must
     * to teach network to find dependence between this data set's
     *
     * net(j) = w0+sum(x(i)*w(i,j))
     * w - weights of edges
     * x - input data
     * w0- weight of j-th node of layer
     * @param teachMatrix - matrix with default figures or letters or digits
     */
    public static BackLearningBean backLearning(double[][] teachMatrix){
        final double etha = 0.3;
        final double alpha = 0.1;
        final double eps = 0.001;

        //teachMatrix = transpositionOfMatrix(teachMatrix);
        final int xs = teachMatrix[0].length;
        final int ys = teachMatrix.length;
        //matrix of numbers of 'ideal' figures to teach NN
        double[][] y = eye(ys, 1);
        //generating start weights of edges between layers of neural network
        double[][] weightIn = matrixPlusNumber(matrixMultiplyByNumber(makeRandomMatrix(xs + 1, 7), 0.6), -0.3);
        double[][] weightOut = matrixPlusNumber(matrixMultiplyByNumber(makeRandomMatrix(8, ys), 0.6), -0.3);

        //zeros to matrix of errors of layers
        double[][] deltaWeightInPrev = createAndFillMatrix(xs + 1, 7, 0);
        double[][] deltaWeightOutPrev = createAndFillMatrix(8, ys, 0);

        int iteration = 0;
        double error = 1;
        double minError = 1;
        //while error of recognition bigger
        //than some number teach our network
        System.out.println("Teaching neural network...");
        while (error > eps && iteration < 50000){
            int i = iteration%ys;
            // get i-th row (for i-th figure)
            double[] in = concat2Vectors(new double[]{1}, teachMatrix[i]);

            //calc output with current weights
            final BackLearningBean tmp = directStep (in, weightIn, weightOut, ys);
            final double[] sums = tmp.getSums();
            final double[] out = tmp.getOut();

            //deltaOut = (y(:,i)-out).*out.*(1-out);
            double[] deltaOut = matrixMultiplyMatrixOneByOne(matrixMultiplyMatrixOneByOne(
                            matrixPlusMatrixOneByOne(y[i], minusMatrix(out)), out), matrixPlusNumber(minusMatrix(out), 1));
            //deltaSums = sums.*(1-sums).*(weightOut*deltaOut);
            double[] deltaSums = matrixMultiplyMatrixOneByOne(matrixMultiplyMatrixOneByOne(
                            sums, matrixPlusNumber(minusMatrix(sums), 1)), matrixMultiplyVector(weightOut, deltaOut));

            //deltaWeightOut = etha*(repmat(H,1,ys).*repmat(deltaOut',8,1)) + alpha*deltaWeightOutPrev;
            double[][] deltaWeightOut = matrixPlusMatrixOneByOne(
                    matrixMultiplyByNumber(matrixMultiplyMatrixOneByOne(replicateMatrix(sums, 1, ys), transpositionOfMatrix(replicateMatrix(deltaOut, 1, 8))), etha),
                    matrixMultiplyByNumber(deltaWeightOutPrev, alpha));
            //deltaWeightIn = etha*(repmat(In,1,7).*repmat(deltaSums(2:length(deltaSums))',xs+1,1)) + alpha*deltaWeightInPrev;
            double[][] deltaWeightIn = matrixPlusMatrixOneByOne(
                    matrixMultiplyByNumber(matrixMultiplyMatrixOneByOne(replicateMatrix(in, 1, 7), transpositionOfMatrix(replicateMatrix(copyPartOfVector(deltaSums, 1, deltaSums.length - 1), 1, xs + 1))), etha),
                    matrixMultiplyByNumber(deltaWeightInPrev, alpha));

            // save delta's errors to next step
            deltaWeightOutPrev = deltaWeightOut;
            deltaWeightInPrev = deltaWeightIn;

            // correcting weights
            weightIn = matrixPlusMatrixOneByOne(weightIn, deltaWeightIn);
            weightOut = matrixPlusMatrixOneByOne(weightOut, deltaWeightOut);

            //calc sum of all elements of matrix of errors
            error = sum(sum(absMatrix(deltaWeightIn))) + sum(sum(absMatrix(deltaWeightOut)));
            iteration++;
            if(0 == iteration%200){
                System.out.print(".");
            }
            if(0 == iteration%(400*50)){
                System.out.println();
            }
            if(minError > error){
                minError = error;
            }
        }
        System.out.println("\nTime of teaching of network: " + iteration + " iterations");
        return new BackLearningBean(weightIn, weightOut, ys);
    }
}