package ua.edu.cdu.appliedMathematics.dm.neuralNetwork.backPropagationFromCSharp;

public class NeuralNetwork {
    public static final double SIGMOID_FUNCTION_X = 45;
    public static final double HYPER_TAN_FUNCTION_X = 10;
    private int numInput;
    private int numHidden;
    private int numOutput;

    private double[] inputs;
    private double[][] ihWeights; // input-to-hidden
    private double[] ihSums;
    private double[] ihBiases;
    private double[] ihOutputs;

    private double[][] hoWeights;  // hidden-to-output
    private double[] hoSums;
    private double[] hoBiases;
    private double[] outputs;

    private double[] oGrads; // output gradients for back-propagation
    private double[] hGrads; // hidden gradients for back-propagation

    private double[][] ihPrevWeightsDelta;  // for momentum with back-propagation
    private double[] ihPrevBiasesDelta;

    private double[][] hoPrevWeightsDelta;
    private double[] hoPrevBiasesDelta;

    public NeuralNetwork(int numInput, int numHidden, int numOutput) {
        this.numInput = numInput;
        this.numHidden = numHidden;
        this.numOutput = numOutput;

        inputs = new double[numInput];
        ihWeights = Helpers.makeMatrix(numInput, numHidden);
        ihSums = new double[numHidden];
        ihBiases = new double[numHidden];
        ihOutputs = new double[numHidden];
        hoWeights = Helpers.makeMatrix(numHidden, numOutput);
        hoSums = new double[numOutput];
        hoBiases = new double[numOutput];
        outputs = new double[numOutput];

        oGrads = new double[numOutput];
        hGrads = new double[numHidden];

        ihPrevWeightsDelta = Helpers.makeMatrix(numInput, numHidden);
        ihPrevBiasesDelta = new double[numHidden];
        hoPrevWeightsDelta = Helpers.makeMatrix(numHidden, numOutput);
        hoPrevBiasesDelta = new double[numOutput];
    }

    /**
     * update the weights and biases using back-propagation, with target values, eta (learning rate), alpha (momentum)
     * @param tValues
     * @param eta
     * @param alpha
     */
    public void updateWeights(final double[] tValues, final double eta, final double alpha){
        // assumes that setWeights and computeOutputs have been called and so all the internal arrays and matrices have values (other than 0.0)
        if (tValues.length != numOutput){
            throw new RuntimeException("target values not same length as output in updateWeights");
        }

        // 1. compute output gradients
        for (int i = 0; i < oGrads.length; ++i) {
            // derivative of tan
            final double derivative = (1 - outputs[i]) * (1 + outputs[i]);
            oGrads[i] = derivative * (tValues[i] - outputs[i]);
        }

        // 2. compute hidden gradients
        for (int i = 0; i < hGrads.length; ++i) {
            // (1 / 1 + exp(-x))'  -- using output value of neuron
            final double derivative = (1 - ihOutputs[i]) * ihOutputs[i];
            double sum = 0;
            // each hidden delta is the sum of numOutput terms
            for (int j = 0; j < numOutput; ++j){
                // each downstream gradient * outgoing weight
                sum += oGrads[j] * hoWeights[i][j];
            }
            hGrads[i] = derivative * sum;
        }

        // 3. update input to hidden weights (gradients must be computed right-to-left but weights can be updated in any order
        for (int i = 0; i < ihWeights.length; ++i) {// 0..2 (3)
            for (int j = 0; j < ihWeights[0].length; ++j) {// 0..3 (4)
                final double delta = eta * hGrads[j] * inputs[i]; // compute the new delta
                ihWeights[i][j] += delta; // update
                ihWeights[i][j] += alpha * ihPrevWeightsDelta[i][j]; // add momentum using previous delta. on first pass old value will be 0.0 but that's OK.
            }
        }

        // 3b. update input to hidden biases
        for (int i = 0; i < ihBiases.length; ++i) {
            final double delta = eta * hGrads[i] * 1.0; // the 1.0 is the constant input for any bias; could leave out
            ihBiases[i] += delta;
            ihBiases[i] += alpha * ihPrevBiasesDelta[i];
        }

        // 4. update hidden to output weights
        for (int i = 0; i < hoWeights.length; ++i){  // 0..3 (4)
            for (int j = 0; j < hoWeights[0].length; ++j){ // 0..1 (2)
                final double delta = eta * oGrads[j] * ihOutputs[i];  // see above: ihOutputs are inputs to next layer
                hoWeights[i][j] += delta;
                hoWeights[i][j] += alpha * hoPrevWeightsDelta[i][j];
                hoPrevWeightsDelta[i][j] = delta;
            }
        }

        // 4b. update hidden to output biases
        for (int i = 0; i < hoBiases.length; ++i) {
            double delta = eta * oGrads[i] * 1.0;
            hoBiases[i] += delta;
            hoBiases[i] += alpha * hoPrevBiasesDelta[i];
            hoPrevBiasesDelta[i] = delta;
        }
    }

    /**
     * copy weights and biases in weights[] array to i-h weights, i-h biases, h-o weights, h-o biases
     * @param weights
     */
    public void setWeights(final double[] weights) {
        final int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
        if (weights.length != numWeights){
            throw new RuntimeException("The weights array length: " + weights.length + " does not match the total number of weights and biases: " + numWeights);
        }

        // points into weights param
        int k = 0;
        for (int i = 0; i < numInput; ++i){
            for (int j = 0; j < numHidden; ++j){
                ihWeights[i][j] = weights[k++];
            }
        }
        for (int i = 0; i < numHidden; ++i){
            ihBiases[i] = weights[k++];
        }
        for (int i = 0; i < numHidden; ++i){
            for (int j = 0; j < numOutput; ++j){
                hoWeights[i][j] = weights[k++];
            }
        }
        for (int i = 0; i < numOutput; ++i){
            hoBiases[i] = weights[k++];
        }
    }

    /**
     * return weights of connections 
     * @return
     */
    public double[] getWeights() {
        final int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
        final double[] result = new double[numWeights];
        int k = 0;
        for (double[] ihWeight : ihWeights) {
            for (int j = 0; j < ihWeights[0].length; ++j) {
                result[k++] = ihWeight[j];
            }
        }
        for (double ihBiase : ihBiases) {
            result[k++] = ihBiase;
        }
        for (double[] hoWeight : hoWeights){
            for (int j = 0; j < hoWeights[0].length; ++j){
                result[k++] = hoWeight[j];
            }
        }
        for (double hoBiase : hoBiases) {
            result[k++] = hoBiase;
        }
        return result;
    }

    /**
     * Computing outputs vector
     * @param xValues
     * @return
     */
    public double[] computeOutputs(double[] xValues) {
        if (xValues.length != numInput){
            throw new RuntimeException("Inputs array length " + inputs.length + " does not match NN numInput value " + numInput);
        }
        for (int i = 0; i < numHidden; ++i){
            ihSums[i] = 0;
        }
        for (int i = 0; i < numOutput; ++i){
            hoSums[i] = 0;
        }
        // copy x-values to inputs
        System.arraycopy(xValues, 0, this.inputs, 0, xValues.length);
        // compute input-to-hidden weighted sums
        for (int j = 0; j < numHidden; ++j){
            for (int i = 0; i < numInput; ++i){
                ihSums[j] += this.inputs[i] * ihWeights[i][j];
            }
        }
        // add biases to input-to-hidden sums
        for (int i = 0; i < numHidden; ++i){
            ihSums[i] += ihBiases[i];
        }
        // determine input-to-hidden output
        for (int i = 0; i < numHidden; ++i){
            ihOutputs[i] = sigmoidFunction(ihSums[i]);
        }
        // compute hidden-to-output weighted sums
        for (int j = 0; j < numOutput; ++j){
            for (int i = 0; i < numHidden; ++i){
                hoSums[j] += ihOutputs[i] * hoWeights[i][j];
            }
        }
        // add biases to input-to-hidden sums
        for (int i = 0; i < numOutput; ++i){
            hoSums[i] += hoBiases[i];
        }
        // determine hidden-to-output result
        for (int i = 0; i < numOutput; ++i){
            this.outputs[i] = hyperTanFunction(hoSums[i]);
        }

        // could define a getOutputs method instead
        final double[] result = new double[numOutput];
        System.arraycopy(this.outputs, 0, result, 0, this.numOutput);
        return result;
    }

    /**
     * an activation function that isn't compatible with back-propagation because it isn't differentiable
     * @param x
     * @return 1 or 0
     */
    private static double stepFunction(final double x){
        return x > 0 ? 1 : 0;
    }

    /**
     * calculating sigmoid function y(x) at some point x
     * @param x
     * @return 0, 1 or 1 / (1 + e^(-x))
     */
    private static double sigmoidFunction(final double x) {
        if (x < -SIGMOID_FUNCTION_X){
            return 0;
        } else if (x > SIGMOID_FUNCTION_X) {
            return 1;
        } else {
            return 1.0 / (1.0 + Math.exp(-x));
        }
    }

    /**
     * calculating hyperbolic tangent function Y(x) at some point X
     * @param x
     * @return -1, 1 or tan(x)
     */
    private static double hyperTanFunction(final double x) {
        if (x < -HYPER_TAN_FUNCTION_X) {
            return -1;
        } else if (x > HYPER_TAN_FUNCTION_X) {
            return 1;
        } else {
            return Math.tanh(x);
        }
    }

    /**
     * sum absolute error
     * @param target
     * @param output
     * @return
     */
    public static double error(final double[] target, final double[] output){
        double sum = 0;
        for (int i = 0; i < target.length; ++i){
            sum += Math.abs(target[i] - output[i]);
        }
        return sum;
    }
}