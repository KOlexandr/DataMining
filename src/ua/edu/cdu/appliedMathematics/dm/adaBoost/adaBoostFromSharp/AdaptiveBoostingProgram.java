package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostFromSharp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * demo program for classification and prediction using adaptive boosting
 */
public class AdaptiveBoostingProgram {
    public static void main(String[] args){
        System.out.println("\nBegin adaptive boosting classification demo\n");

        final String[] features = new String[] { "Opponent", "Field", "Spread", "Result" };             // dependent feature to predict is last listed

        final String[][] values = new String[4][];
        values[0] = new String[] { "Atlanta", "Buffalo", "Chicago", "Detroit" };                        // opponent
        values[1] = new String[] { "Home", "Away" };                                                    // field
        values[2] = new String[] { "Small ", "Medium", "Large " };                                      // point spread (note: some spaces added)
        values[3] = new String[] { "Lose", "Win" };                                                     // the dependent/predicted variable

        final String[][] rawTrain = new String[10][];                                                   // typically training data would be in a text file or SQL table
        rawTrain[0] = new String[] { "Detroit", "Home", "Large ", "Win" };
        rawTrain[1] = new String[] { "Detroit", "Away", "Medium", "Win" };
        rawTrain[2] = new String[] { "Buffalo", "Home", "Small ", "Win" };
        rawTrain[3] = new String[] { "Buffalo", "Home", "Medium", "Win" };
        rawTrain[4] = new String[] { "Atlanta", "Away", "Large ", "Win" };
        rawTrain[5] = new String[] { "Chicago", "Home", "Medium", "Win" };

        rawTrain[6] = new String[] { "Chicago", "Away", "Small ", "Lose" };
        rawTrain[7] = new String[] { "Chicago", "Home", "Small ", "Lose" };
        rawTrain[8] = new String[] { "Atlanta", "Away", "Medium", "Lose" };
        rawTrain[9] = new String[] { "Detroit", "Away", "Large ", "Lose" };

        System.out.println("Raw (String) training data for team Seattle:\n");
        System.out.println("Opponent Field  Spread   Result");
        System.out.println("===============================");
        showMatrix(rawTrain);

        System.out.println("\nConverting and storing training data.");
        final int[][] train = rawTrainToInt(rawTrain, values);                                                    // training tuples stored as ints in a matrix; last col (Y) as -1 or +1
        System.out.println("Training data in int form:\n");
        showMatrix(train, true);

        System.out.println("\nCreating weak categorical stump learners from training data.");
        final List<Learner> learners = makeLearners(values, train);
        System.out.println("Completed. Weak learners are:\n");
        for (int i = 0; i < learners.size(); ++i){
            System.out.println("[" + i + "] " + description(learners.get(i), features, values));
        }

        System.out.println("\nInitializing list of best learner indexes.");
        final List<Integer> bestLearners = new ArrayList<>();                                                     // indexes of best weak learner found for each iteration t in main algorithm loop

        System.out.println("\nUsing adaptive boosting to find best learners and their alpha values.");
        makeModel(train, learners, bestLearners);                                                                 // find best learners and their alphas (importance)

        System.out.println("\nModel completed.");
        final int numGood = bestLearners.size();
        System.out.println("Algorithm found " + numGood + " good learners and associated alpha values.");         // some weak learners may not make the cut

        System.out.println("\nThe good learners and their alpha value are:");
        for (Integer bestLearner : bestLearners) {
            System.out.print("[" + bestLearner + "] " + learners.get(bestLearner).getAlpha() + "  ");
        }

        //System.out.println("\nPredicting outcome when Opponent = Buffalo, Field = Away, Spread = Small\n");
        //int[] unknownTuple = new int[] { 1, 1, 0 };    // Buffalo, Away, Small

        System.out.println("\nPredicting outcome when Opponent = Detroit, Field = Home, Spread = Small.\n");
        final int[] unknownTuple = new int[] { 3, 0, 0 };    // Detroit, Home, Small

        final int y = classify(unknownTuple, learners, bestLearners);
        System.out.println("Predicted Y = " + y + " => Seattle will " + yValueToString(y, values));

        System.out.println("\nEnd\n");
    }

    /**
     * data conversion routines below
     */
    private static int[][] rawTrainToInt(final String[][] rawTrain, final String[][] values){
        // converts raw training data in String form to 0-based ints; last col to -1 or +1
        // calls helper valueToInt
        final int rows = rawTrain.length;
        final int cols = rawTrain[0].length;
        final int[][] result = new int[rows][cols];

        for (int i = 0; i < rawTrain.length; ++i){
            for (int j = 0; j < rawTrain[i].length; ++j){               // all cols
                result[i][j] = valueToInt(rawTrain[i][j], j, values);   // String to int
            }
        }

        for (int i = 0; i < result.length; ++i){                        // convert values in last col from 0,1 to -1,+1
            if (result[i][cols - 1] == 0) result[i][cols - 1] = -1;
        }
        return result;
    }

    /**
     * convert a String value like "Detroit" to an int (3)
     */
    private static int valueToInt(final String value, final int feature, final String[][] values){
        for (int j = 0; j < values[feature].length; ++j){
            if (values[feature][j].equals(value)){
                return j;
            }
        }
        return -1; // error
    }

    /**
     * convert an int value like 2 to a String ("Chicago")
     */
    private static String intToValue(final int value, final int feature, final String[][] values){
        return values[feature][value];
    }

    /**
     * convert -1 or +1 to a String like "Lose" or "Win"
     */
    private static String yValueToString(final int y, final String[][] values){
        if (y == -1) {
            return values[values.length - 1][0];
        } else if (y == 1) {
            return values[values.length - 1][1];
        } else {
            return "error";
        }
    }

    /**
     * convert a String feature like "Opponent" to an int (0)
     */
    private static int featureToInt(final String feature, final String[] features){
        for (int i = 0; i < features.length; ++i){
            if (features[i].equals(feature)){
                return i;
            }
        }
        return -1;  // error
    }

    /**
     * convert an int like 1 to a String ("Field")
     */
    private static String intToFeature(final int feature, final String[] features){
        return features[feature];
    }

    /**
     * learner-making routines below. see class Learner too.
     * return a friendly String description of a weak Learner
     */
    private static String description(final Learner learner, final String[] features, final String[][] values){
        final String feature = intToFeature(learner.getFeature(), features);                    // like "Opponent"
        final String value = intToValue(learner.getValue(), learner.getFeature(), values);      // like "Atlanta"
        final String dependent = features[features.length - 1];
        String predicted;
        if (learner.getPredicted() == 1){
            predicted = values[values.length - 1][1];
        } else {                                                                                // predicted == -1
            predicted = values[values.length - 1][0];
        }
        return "IF " + feature + " IS " + value + " THEN " + dependent + " IS " + predicted + "   (raw error = " + learner.getError() + ")";
    }

    /**
     * scan through training set and try to make one weak learner for each possible feature value
     * do not create a learner for values with raw error rate of 0.50 or greater
     */
    private static List<Learner> makeLearners(final String[][] values, final int[][] train){
        final int numFeatures = values.length;                              // includes the dependent variable
        final List<Learner> result = new ArrayList<>();

        for (int f = 0; f < numFeatures - 1; ++f){                          // each predictive feature (but not Y)
            int numValues = values[f].length;                               // num values for curr feature
            for (int v = 0; v < numValues; ++v){                            // each value of curr feature
                int prev = mostLikely(f, v, train);                         // most likely Y (-1,+1) for v; 0 if no most likely can be determined or error >= 0.50
                if (prev == 0) {
                    continue;                                               // 0 means that we could not make a prediction for this value, so we won't make a Learner for this feature value
                }
                Learner lrn = new Learner(f, v, prev, -1.0, -1.0, -1.0);    // tmp error -1.0, tmp epsilon -1.0, tmp alpha = -1.0
                result.add(lrn);
            }
        }
        // for each learner, scan training data and compute raw error rate
        for (Learner aResult : result) {                                    // each learner
            int nw = 0;                                                     // number wrong
            int nr = 0;                                                     // number relevant
            for (int[] aTrain : train) {                                    // each training tuple
                if (!isApplicable(aResult, aTrain)) {
                    continue;                                               // the curr rule doesn't apply to curr tuple so skip
                }
                ++nr;
                int act = aTrain[aTrain.length - 1];                        // actual -1 or +1 is value in last column of training matrix
                if (aResult.getPredicted() != act) {
                    ++nw;                                                   // wrong
                }
            }
            if (nr == 0) {
                aResult.setError(0);                                        // consider throWing an Exception here
            } else {
                aResult.setError((nw * 1.0) / nr);
            }
        }
        return result;
    }

    /**
     * highest probability Y value for a particular feature value (like "field" is "Home")
     * return -1 or +1 based on highest prob,
     * return 0 if equal percentages (0.5 and 0.5) or no relevant training data
     * used to determine the predicted Y field in a Learner object in method makeLearners
     */
    private static int mostLikely(final int feature, final int value, final int[][] train){
        int ctMinusOne = 0;                                 // relevant count of -1 values: number training tuples where value has Y = -1
        int ctPlusOne = 0;                                  // relevant count +1 values
        int totRelevant = 0;
        int cols = train[0].length;

        for (int[] aTrain : train) {                        // each training tuple
            if (aTrain[feature] != value) {
                continue;                                   // this tuple not relevant because wrong value
            }
            ++totRelevant;                                  // we know curr tuple is relevant
            if (aTrain[cols - 1] == -1) {                   // the last column is the dependent variable (like sex)
                ++ctMinusOne;
            } else {
                ++ctPlusOne;
            }
        }

        if (totRelevant == 0) {
            return 0;                                       // there were no relevant training tuples for the feature value so cannot determine a most likely Y
        }
        if (ctMinusOne > 0 && ctPlusOne == 0) {
            return -1;                                      // there was at least one -1 but no +1 values so most likely Y is -1
        }
        if (ctMinusOne == 0 && ctPlusOne > 0) {
            return 1;                                       // see comment above
        }

        double pctMinusOne = (ctMinusOne * 1.0) / totRelevant;      // percentage or probability that Y is -1
        double pctPlusOne = (ctPlusOne * 1.0) / totRelevant;        // percentage or probability that Y is +1

        if (veryCLose(pctMinusOne, pctPlusOne)) {
            return 0;                                        // both pcts are 0.5000 so no most likely Y
        }

        if (pctMinusOne > pctPlusOne) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * model-building routines below
     */
    private static void updateEpsilons(final List<Learner> learners, final int[][] train, final double[] trainWeights){
        // for each learner, update its epsilon by walking thru each training tuple and computing a weighted error sum
        for (Learner learner : learners) {
            double ep = 0.0;
            for (int k = 0; k < train.length; ++k) {                    // each training tuple
                if (!isApplicable(learner, train[k])) {
                    continue;                                           //  curr learner doesn't apply to curr tuple so move to next tuple
                }
                int actual = train[k][train[k].length - 1];             // actual -1 +1 is in last col of training matrix
                int prev = learner.getPredicted();                      // predicted -1 +1
                if (actual != prev) {                                   // incorrect prediction so . .
                    ep = ep + trainWeights[k];                          // accumulate weighted error
                }
            }
            learner.setEpsilon(ep);                                     // could be zero -- must deal with before computing alpha
        }
    }

    /**
     * is the learner/weak classifer applicable to the training tuple?
     */
    private static boolean isApplicable(final Learner learner, final int[] tuple){
        return tuple[learner.getFeature()] == learner.getValue();
    }

    /**
     * find index of not-yet-used learner that has Smallest epsilon (weighted error)
     * update usedLearners hash table array
     */
    private static double[] bestLearner(final List<Learner> learners, final int[] usedLearners){
        // check to make sure there is at least one unused weak learner
        Arrays.sort(usedLearners);
        int x = Arrays.binarySearch(usedLearners, 0);                           // find first unused learner
        if (x < 0) {
            throw new RuntimeException("No unused weak learners found by method bestLearner");
        }

        double smallestEpsilon = Double.MAX_VALUE;                              // Smaller epsilon values mean Smaller error which means better learner
        int bestIndex = -1;
        for (int i = 0; i < learners.size(); ++i){
            if (usedLearners[i] == 1) {
                continue;                                                       // curr weak learner has already been used
            }
            if (learners.get(i).getEpsilon() < smallestEpsilon){
                smallestEpsilon = learners.get(i).getEpsilon();
                bestIndex = i;
            }
        }

        if (smallestEpsilon < 0){
            throw new RuntimeException("Encountered a negative epsilon in method bestLearner");
        }
        if (bestIndex == -1){
            throw new RuntimeException("Unable to find best learner index in method bestLearner");
        }
        usedLearners[bestIndex] = 1;                                            // flag the best weak learner as used
        return new double[]{bestIndex, smallestEpsilon};
    }

    private static boolean cLoseToZero(final double x){
        return Math.abs(x) < 0.00000001;
    }

    private static boolean veryCLose(final double x, final double y){
        return Math.abs(x - y) < 0.00000001;
    }

    /**
     * increase weights of misclassified (by curr best learner) training tuples, decrease weights of correctly classified tuples
     */
    private static void updateTrainingWeights(final double[] trainWeights, final int[][] train, final List<Learner> learners, final double alphaT, final int bestIndex){
        int cols = train[0].length;                                                 // number columns of training data matrix

        for (int i = 0; i < trainWeights.length; ++i){                              // each weight and each training tuple
            if (!isApplicable(learners.get(bestIndex), train[i])) {
                continue;
            }
            int actual = train[i][cols - 1];                                        // -1 or +1
            trainWeights[i] = trainWeights[i] * Math.exp(-alphaT * actual * learners.get(bestIndex).getPredicted());  // tricky; depends on -1 +1 encoding for Y
        }

        double z = 0;                                                               // sum of all D weights to normalize them
        for (double trainWeight : trainWeights) {                                   // each updated weight
            z += trainWeight;
        }

        for (int i = 0; i < trainWeights.length; ++i){                              // each updated weight
            trainWeights[i] = trainWeights[i] / z;                                  // normalize (so all weights now sum to 1.0)
        }
    }

    /**
     * compute (indexes of) best learners and their alphas
     * assumes all weak learners have been created
     * assumes bestLearners (index List) has been initialized to a empty List
     */
    private static void makeModel(final int[][] train, final List<Learner> learners, final List<Integer> bestLearners){
        System.out.println("\nInitializing training tuple weights ('D') array.");
        final double[] trainWeights = new double[train.length];                                     // training weights (D values) are local because they aren't needed to classify
        for (int i = 0; i < trainWeights.length; ++i){                                              // one weight per tuple
            trainWeights[i] = 1.0 / trainWeights.length;                                            // initially all weights equal
        }
        //ShowVector(trainWeights);

        final int[] usedLearners = new int[learners.size()];                                        // track which weak learners have been added to bestLearners List. index = which learner, value 0 = not used, 1 = used
        System.out.println("Entering main algorithm loop.");
        boolean done = false;
        int t = 0;                                                                                  // not sure why research literature uses t for counter variable name here . . .
        while (!done){
            //System.out.println("================");
            //System.out.println("t = " + t);
            updateEpsilons(learners, train, trainWeights);                                          // compute weighted error for each learner
            //for (int j = 0; j < learners.Count; ++j)
            //  System.out.println(learners[j].ToString());

            final double[] doubles = bestLearner(learners, usedLearners);
            final int bestIndex = (int) doubles[0];                                                 // find index of best learner (that hasn't yet been used) and its weighted error epsilonT
            double epsilonT = doubles[1];                                                           // best (Smallest) weighted error at curr iteration t
            //System.out.println("best learner is " + bestIndex); Console.ReadLine();
            if (Math.abs(0.5 - epsilonT) <= 0.00001) {
                break;                                                                              // if best weighted error is (almost) 0.5 or greater then no use in continuing on
            }
            bestLearners.add(bestIndex);

            if (cLoseToZero(epsilonT)) {
                epsilonT = 0.000001;                                                                // avoid division by 0 when computing alphaT
            }
            final double alphaT = 0.5 * Math.log((1.0 - epsilonT) / epsilonT);                      // Smaller epsilon error makes Larger alpha values
            //System.out.println("a = " + alphaT); Console.ReadLine();
            learners.get(bestIndex).setAlpha(alphaT);                                               // store the alpha

            updateTrainingWeights(trainWeights, train, learners, alphaT, bestIndex);                // update weights of training tuples that are relevant to best learner

            ++t;
            if (t == learners.size()) {
                done = true;                                                                        // every weak learner has been used
            }
            //System.out.println("================");
        }
        Collections.sort(bestLearners);                                                             // sort this List of indexes to make the output easier to read. not absolutely necessary
    }

    /**
     * use adaptive boosting model (set of weak learners and their alpha (importtance) weights) to predict Y for a testTuple (in int form)
     */
    private static int classify(final int[] testTuple, final List<Learner> learners, final List<Integer> bestLearners){
        double accVote = 0;                                                                                     // accumulated votes of learners
        if (bestLearners.size() == 0) {
            throw new RuntimeException("No learners found in List of bestLearners");
        }

        for (Integer bestLearner : bestLearners) {
            int idx = bestLearner;                                                                              // index of curr learner
            if (!isApplicable(learners.get(idx), testTuple)) {
                continue;                                                                                       // curr learner can't vote on the tuple
            }

            //double vote = alphas[t] * learners[idx].predicted;
            double vote = learners.get(idx).getPredicted() * learners.get(idx).getAlpha();
            accVote += vote;
            String sign = "";
            if (learners.get(idx).getPredicted() == 1) {
                sign = "+";
            }
            //System.out.println("Using learner " + idx + " with alpha = " + alphas[t].ToString("F2") + " prediction is " + sign + learners[idx].predicted + " so vote = " + vote.ToString("F2") );
            System.out.println("Using learner " + idx + " with alpha = " + learners.get(idx).getAlpha() + " prediction is " + sign + learners.get(idx).getPredicted() + " so vote = " + vote);
        }

        System.out.println("\nFinal accumulated vote is " + accVote);
        if (accVote > 0) {
            return 1;
        } else if (accVote < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    // display routines below

    private static void showVector(final String[] vector) {
        for (String aVector : vector) System.out.print(aVector + " ");
        System.out.println();
    }

    private static void showVector(final int[] vector){
        for (int aVector : vector) System.out.print(aVector + " ");
        System.out.println();
    }

    private static void showVector(final double[] vector) {
        for (double aVector : vector) System.out.print(aVector + " ");
        System.out.println();
    }

    private static void showMatrix(final String[][] matrix) {
        for (String[] aMatrix : matrix) {
            for (String anAMatrix : aMatrix) {
                System.out.print(anAMatrix + "\t");
            }
            System.out.println();
        }
    }

    private static void showMatrix(final int[][] matrix, final boolean isTrain) {
        for (int[] aMatrix : matrix) {
            for (int j = 0; j < aMatrix.length - 1; ++j) {
                System.out.print(aMatrix[j] + " ");
            }
            if (isTrain) {
                System.out.print(" ->  ");
                if (aMatrix[aMatrix.length - 1] == -1) {
                    System.out.print("-1");
                } else {
                    System.out.print("+1");
                }
            }
            System.out.println();
        }
    }
}