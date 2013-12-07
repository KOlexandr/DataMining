package ua.edu.cdu.appliedMathematics.dm.neuralNetwork;

public class BackLearningBean {
    private double[][] weightIn;
    private double[][] weightOut;
    private int ys;
    private double[] out;
    private double[] sums;

    public BackLearningBean(double[][] weightIn, double[][] weightOut, int ys) {
        this.weightIn = weightIn;
        this.weightOut = weightOut;
        this.ys = ys;
    }

    public BackLearningBean(double[] out, double[] sums) {
        this.out = out;
        this.sums = sums;
    }

    public double[][] getWeightIn() {
        return weightIn;
    }

    public void setWeightIn(double[][] weightIn) {
        this.weightIn = weightIn;
    }

    public double[][] getWeightOut() {
        return weightOut;
    }

    public void setWeightOut(double[][] weightOut) {
        this.weightOut = weightOut;
    }

    public int getYs() {
        return ys;
    }

    public void setYs(int ys) {
        this.ys = ys;
    }

    public double[] getOut() {
        return out;
    }

    public void setOut(double[] out) {
        this.out = out;
    }

    public double[] getSums() {
        return sums;
    }

    public void setSums(double[] sums) {
        this.sums = sums;
    }
}
