package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostFromSharp;

/**
 * weak learner
 */
public class Learner {

    private int value;                  // value: Atlanta, . . Home, . . Small, . .
    private int feature;                // which feature: Opponent, Field, Spread
    private double alpha;               // importance weight
    private double error;               // actual error rate is percentage of tuples incorrectly classified
    private int predicted;              // -1 (Lose) or +1 (Win)
    private double epsilon;             // weighted error (because different training tuples have different weights)

    public Learner(int feature, int value, int predicted, double error, double epsilon, double alpha) {
        this.value = value;             // ex: "Chicago"
        this.error = error;             // ex: 0.45 (cannot be greater than 0.50)
        this.alpha = alpha;             // ex: 2.36
        this.feature = feature;         // ex: "Opponent"
        this.epsilon = epsilon;         // ex: 0.15 but not 0.65 (i.e., not greater than 0.50)
        this.predicted = predicted;     // -1 or +1 (Lose, Win)
    }

    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append("feature = ").append(feature);
        s.append(" value = ").append(value);
        s.append(" predicted = ").append((predicted == 1.0) ? "+" : "").append(predicted);
        s.append(" error = ").append(error);
        s.append(" epsilon = ").append(epsilon);
        s.append(" alpha = ").append(alpha);
        return s.toString();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public int getPredicted() {
        return predicted;
    }

    public void setPredicted(int predicted) {
        this.predicted = predicted;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
