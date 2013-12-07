package ua.edu.cdu.appliedMathematics.dm.neuralNetwork.backPropagationFromCSharp;

public class Main {
    public static void main(String[] args) {
        System.out.println("Begin Neural Network Back-Propagation demo\n");

        System.out.println("Creating a 3-input, 4-hidden, 2-output neural network");
        System.out.println("Using sigmoid function for input-to-hidden activation");
        System.out.println("Using tanh function for hidden-to-output activation");
        final NeuralNetwork nn = new NeuralNetwork(3, 4, 2);

        // arbitrary weights and biases
        final double[] weights = new double[] {
                0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2,
                -2.0, -6.0, -1.0, -7.0,
                1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0,
                -2.5, -5.0 };

        System.out.println("\nInitial 26 random weights and biases are:");
        Helpers.showVector(weights, 2, true);

        System.out.println("Loading neural network weights and biases");
        nn.setWeights(weights);

        System.out.println("\nSetting inputs:");
        final double[] xValues = new double[] { 1.0, 2.0, 3.0 };
        Helpers.showVector(xValues, 2, true);

        final double[] initialOutputs = nn.computeOutputs(xValues);
        System.out.println("Initial outputs:");
        Helpers.showVector(initialOutputs, 4, true);

        final double[] tValues = new double[] { -0.8500, 0.7500 }; // target (desired) values. note these only make sense for tanh output activation
        System.out.println("Target outputs to learn are:");
        Helpers.showVector(tValues, 4, true);

        final double eta = 0.90;  // learning rate - controls the maginitude of the increase in the change in weights. found by trial and error.
        final double alpha = 0.04; // momentum - to discourage oscillation. found by trial and error.
        System.out.printf("Setting learning rate (eta) = %.2f and momentum (alpha) = %.2f", eta, alpha);

        System.out.println("\nEntering main back-propagation compute-update cycle");
        System.out.println("Stopping when sum absolute error <= 0.01 or 1,000 iterations\n");
        int ctr = 0;
        double[] yValues = nn.computeOutputs(xValues); // prime the back-propagation loop
        double error = NeuralNetwork.error(tValues, yValues);
        while (ctr < 1000 && error > 0.01){
            System.out.println("===================================================");
            System.out.println("iteration = " + ctr);
            System.out.println("Updating weights and biases using back-propagation");
            nn.updateWeights(tValues, eta, alpha);
            System.out.println("Computing new outputs:");
            yValues = nn.computeOutputs(xValues);
            Helpers.showVector(yValues, 4, false);
            System.out.println("\nComputing new error");
            error = NeuralNetwork.error(tValues, yValues);
            System.out.printf("Error = %.4f\n", + error);
            ++ctr;
        }
        System.out.println("===================================================");
        System.out.println("\nBest weights and biases found:");
        final double[] bestWeights = nn.getWeights();
        Helpers.showVector(bestWeights, 2, true);
        System.out.println("End Neural Network Back-Propagation demo\n");
    }
}
