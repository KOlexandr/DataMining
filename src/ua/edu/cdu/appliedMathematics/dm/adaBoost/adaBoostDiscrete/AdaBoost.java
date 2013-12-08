package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

import java.util.Arrays;
import java.util.List;

public class AdaBoost {

    /**
     * method classify data
     * @param dataList - list with initial data
     * @param L - list of BaseLearnFunctions which has method apply(Point x)
     * @param T  - number of iterations
     * @return object of class AdaBoostResultFunction that has method apply, object can classify any Point
     */
    public static AdaBoostResultFunction classify(final List<Data> dataList, final List<BaseLearnFunction> L, final int T){
        Integer count = null;
        final int size = dataList.size();
        final double[] e = new double[T];
        final double[] alpha = new double[T];
        final BaseLearnFunction[] h = new BaseLearnFunction[T];

        double[] dCurrent = new double[size];
        double[] dNext = new double[size];
        for (int i = 0; i < size; i++) {
            dCurrent[i] = 1.0/size;
        }
        for (int t = 0; t < T; t++) {
            e[t] = Double.MAX_VALUE;
            //find e[t] = argmin(eTmp[j])
            //eTmp[j] = SUM{i=1,size}(dCurrent[i]*indicatorFunction(data[i].y, h(data[i].x)))
            for (BaseLearnFunction function : L) {
                double eTmp = 0;
                for (int j = 0; j < size; j++) {
                    eTmp += dCurrent[j]*indicatorFunction(dataList.get(j), function);
                }
                if(eTmp < e[t]){
                    e[t] = eTmp;
                    h[t] = function;
                }
            }
            L.remove(h[t]);
            if(e[t] > 0.5) {
                count = t;
                break;
            }
            alpha[t] = 0.5*Math.log((1 - e[t])/e[t]);
            System.out.println("Step = " + t + ", current weight = " + alpha[t]);
            double sum = 0;
            //find dNext[i] = (dCurrent[i]*exp(-alpha[t]*data[i].y*h[t](data[i].x)))/z
            //z must be that SUM{i=1,size}dNext[i] = 1
            for (int i = 0; i < size; i++) {
                //dNext[i] = dCurrent[i] * Math.exp(-dataList.get(i).getY().getDataClassId()*alpha[t]*h[t].apply(dataList.get(i).getX()));
                //dNext[i] = dCurrent[i] * (dataList.get(i).getY().getDataClassId() == indicatorFunction(dataList.get(i), h[t]) ? Math.exp(-dataList.get(i).getY().getDataClassId()*alpha[t]*h[t].apply(dataList.get(i).getX())) : 1);
                //dNext[i] = dCurrent[i] * (dataList.get(i).getY().getDataClassId() == indicatorFunction(dataList.get(i), h[t]) ? Math.exp(-alpha[t]) : Math.exp(alpha[t]));
                dNext[i] = dCurrent[i] * Math.exp(alpha[t]*(2*indicatorFunction(dataList.get(i), h[t])-1));
                sum += dNext[i];
            }
            for (int i = 0; i < size; i++) {
                dNext[i] /= sum;
            }
            dCurrent = dNext;
        }
        final int c = null == count ? T : count;
        System.out.println("\nAlpha array: " + Arrays.toString(Arrays.copyOfRange(alpha, 0, c)) + "\n");
        return new AdaBoostResultFunction() {
            @Override
            public int apply(Point x) {
                double sum = 0;
                //f(x) = sign(SUM{i=1,T}alpha[i]*h[i](x))
                for (int i = 0; i < c; i++){
                    sum += alpha[i]*h[i].apply(x);
                }
                return (int) Math.signum(sum);
            }
        };
    }

    /**
     *
     * @param data - data with Point and DataClass
     * @param f - BaseLearnFunction
     * @return 1 if DataClass of data == result of f(data.getX()) else 0
     */
    private static int indicatorFunction(Data data, BaseLearnFunction f){
        return data.getY().getDataClassId() == f.apply(data.getX()) ? 1 : 0;
    }
}