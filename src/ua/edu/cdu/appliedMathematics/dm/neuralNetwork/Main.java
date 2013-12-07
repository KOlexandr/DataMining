package ua.edu.cdu.appliedMathematics.dm.neuralNetwork;

import static ua.edu.cdu.appliedMathematics.dm.neuralNetwork.utils.OperationsWithMatrixAndVectors.*;

public class Main {
    public static void main(String[] args) {
        //letters
        System.out.println("Symbols:");
        final BackLearningBean symbolsBean = BackLearning.backLearning(intArrayToDouble(symbols));
        for (int[] symbol : symbols) {
            test(symbol, symbolsBean, Type.SYMBOL);
        }
        System.out.println("==============================================================\n");

        //digits
        System.out.println("Digits:");
        final BackLearningBean digitsBean = BackLearning.backLearning(intArrayToDouble(digits));
        for (int[] digit : digits) {
            test(digit, digitsBean, Type.DIGIT);
        }
        System.out.println("==============================================================\n");

        //figures
        final BackLearningBean figuresBean = BackLearning.backLearning(intArrayToDouble(figures));
        System.out.println("Figures:");
        for (int[] figure : figures) {
            test(figure, figuresBean, Type.FIGURE);
        }
        System.out.println("==============================================================\n");
    }

    private static void test(final int[] test, final BackLearningBean bean, final Type type){
        //run this file, input: vector of 'bad' symbol|figures|digits
        final int[] vector = concat2Vectors(new int[]{1}, test);
        final double[] out = BackLearning.directStep(intArrayToDouble(vector), bean.getWeightIn(), bean.getWeightOut(), bean.getYs()).getOut();
        showArray(test, 7);
        switch (type){
            case SYMBOL:
                symbolsTest(max(out));
                break;
            case DIGIT:
                digitsTest(max(out));
                break;
            case FIGURE:
                figuresTest(max(out));
                break;
        }
    }

    private static void symbolsTest(final double[] results){
        final int index = (int)results[0];
        final double value = results[1];
        switch (index){
            case 0:
                System.out.printf("It is %d%% that it is A letter\n", Math.round(value*100));
                break;
            case 1:
                System.out.printf("It is %d%% that it is B letter\n", Math.round(value*100));
                break;
            case 2:
                System.out.printf("It is %d%% that it is C letter\n", Math.round(value*100));
                break;
            case 3:
                System.out.printf("It is %d%% that it is D letter\n", Math.round(value*100));
                break;
            case 4:
                System.out.printf("It is %d%% that it is E letter\n", Math.round(value*100));
                break;
            case 5:
                System.out.printf("It is %d%% that it is F letter\n", Math.round(value*100));
                break;
            case 6:
                System.out.printf("It is %d%% that it is G letter\n", Math.round(value*100));
                break;
            case 7:
                System.out.printf("It is %d%% that it is H letter\n", Math.round(value*100));
                break;
            case 8:
                System.out.printf("It is %d%% that it is I letter\n", Math.round(value*100));
                break;
            case 9:
                System.out.printf("It is %d%% that it is J letter\n", Math.round(value*100));
                break;
            case 10:
                System.out.printf("It is %d%% that it is K letter\n", Math.round(value*100));
                break;
            case 11:
                System.out.printf("It is %d%% that it is L letter\n", Math.round(value*100));
                break;
            case 12:
                System.out.printf("It is %d%% that it is M letter\n", Math.round(value*100));
                break;
            case 13:
                System.out.printf("It is %d%% that it is N letter\n", Math.round(value*100));
                break;
            case 14:
                System.out.printf("It is %d%% that it is O letter\n", Math.round(value*100));
                break;
            case 15:
                System.out.printf("It is %d%% that it is P letter\n", Math.round(value*100));
                break;
            case 16:
                System.out.printf("It is %d%% that it is Q letter\n", Math.round(value*100));
                break;
            case 17:
                System.out.printf("It is %d%% that it is R letter\n", Math.round(value*100));
                break;
            case 18:
                System.out.printf("It is %d%% that it is S letter\n", Math.round(value*100));
                break;
            case 19:
                System.out.printf("It is %d%% that it is T letter\n", Math.round(value*100));
                break;
            case 20:
                System.out.printf("It is %d%% that it is U letter\n", Math.round(value*100));
                break;
            case 21:
                System.out.printf("It is %d%% that it is V letter\n", Math.round(value*100));
                break;
            case 22:
                System.out.printf("It is %d%% that it is W letter\n", Math.round(value*100));
                break;
            case 23:
                System.out.printf("It is %d%% that it is X letter\n", Math.round(value*100));
                break;
            case 24:
                System.out.printf("It is %d%% that it is Y letter\n", Math.round(value*100));
                break;
            case 25:
                System.out.printf("It is %d%% that it is Z letter\n", Math.round(value*100));
                break;
            default:
                System.out.println("Can not recognize letter");
                break;
        }
        System.out.println();
    }

    private static void figuresTest(final double[] results){
        final int index = (int)results[0];
        final double value = results[1];
        switch (index){
            case 0:
                System.out.printf("It is %d%% that it is CROSS figure\n", Math.round(value*100));
                break;
            case 1:
                System.out.printf("It is %d%% that it is SQUARE figure\n", Math.round(value*100));
                break;
            case 2:
                System.out.printf("It is %d%% that it is TRIANGLE figure\n", Math.round(value*100));
                break;
            default:
                System.out.println("Can not recognize figure");
                break;
        }
        System.out.println();
    }

    private static void digitsTest(final double[] results){
        final int index = (int)results[0];
        final double value = results[1];
        switch (index){
            case 0:
                System.out.printf("It is %d%% that it is ZERO figure\n", Math.round(value*100));
                break;
            case 1:
                System.out.printf("It is %d%% that it is ONE figure\n", Math.round(value*100));
                break;
            case 2:
                System.out.printf("It is %d%% that it is TWO figure\n", Math.round(value*100));
                break;
            case 3:
                System.out.printf("It is %d%% that it is THREE figure\n", Math.round(value*100));
                break;
            case 4:
                System.out.printf("It is %d%% that it is FOUR figure\n", Math.round(value*100));
                break;
            case 5:
                System.out.printf("It is %d%% that it is FIVE figure\n", Math.round(value*100));
                break;
            case 6:
                System.out.printf("It is %d%% that it is SIX figure\n", Math.round(value*100));
                break;
            case 7:
                System.out.printf("It is %d%% that it is SEVEN figure\n", Math.round(value*100));
                break;
            case 8:
                System.out.printf("It is %d%% that it is EIGHT figure\n", Math.round(value*100));
                break;
            case 9:
                System.out.printf("It is %d%% that it is NINE figure\n", Math.round(value*100));
                break;
            default:
                System.out.println("Can not recognize figure");
                break;
        }
        System.out.println();
    }

    private static final int[][] symbols = {{0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,1,0,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0,0,1,1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,1,0,1,1,0,0,1,0,1,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,1,0,0,1,0,0,1,1,0,0,1,0,0,1,0,1,0,1,0,0,1,0,0,1,1,0,0,1,0,0,1,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,1,0,1,0,0,1,0,0,1,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,0,0,0,1,0,1,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,1,0,1,0,0,1,1,0,1,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0}};

    private static final int[][] figures = {{0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,},
                            {0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0}};

    private static final int[][] digits = {{0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,1,0,0,0,0,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0}};
}

