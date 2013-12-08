package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final List<Data> data = new ArrayList<>();
        System.out.println("Solving XOR Problem:");
        data.add(new Data(new Point(0,1), DataClass.PLUS));
        data.add(new Data(new Point(0,-1), DataClass.PLUS));
        data.add(new Data(new Point(1,0), DataClass.MINUS));
        data.add(new Data(new Point(-1,0), DataClass.MINUS));
        final AdaBoostResultFunction function = AdaBoost.classify(data, makeBaseLearnFunctions(), 6);
        final Point x = new Point(1, 0);
        System.out.println("Point " + x + " category: " + function.getDataClass(x).toString());
        System.out.println("END");
    }

    /**
     * Create list with BaseLearnFunctions for 7.3.1 from Wu X. Kumar V. The top 10 algorithms in data mining
     * for Solving XOR Problem
     * @return list of BaseLearnFunctions
     */
    private static List<BaseLearnFunction> makeBaseLearnFunctions(){
        final List<BaseLearnFunction> h = new ArrayList<>();
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX1() > -0.5 ? 1 : -1;
            }
            final int idx = 1;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX1() > -0.5 ? -1 : 1;
            }
            final int idx = 2;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX1() > 0.5 ? 1 : -1;
            }
            final int idx = 3;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX1() > 0.5 ? -1 : 1;
            }
            final int idx = 4;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX2() > -0.5 ? 1 : -1;
            }
            final int idx = 5;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX2() > -0.5 ? -1 : 1;
            }
            final int idx = 6;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX2() > 0.5 ? 1 : -1;
            }
            final int idx = 7;
        });
        h.add(new BaseLearnFunction() {
            @Override
            public int apply(Point x) {
                return x.getX2() > 0.5 ? -1 : 1;
            }
            final int idx = 8;
        });
        return h;
    }
}