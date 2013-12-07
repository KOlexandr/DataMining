package ua.edu.cdu.appliedMathematics.modeling.spring;

public class Spring {

    private static double time[];
    private static double coordinates[];

    /**
     * simple harmonic oscillator
     * @param initialDeviation initial deviation of ua.edu.cdu.appliedMathematics.dm.ua.edu.cdu.appliedMathematics.modeling.spring
     * @param coeff - coefficient of stiffness of ua.edu.cdu.appliedMathematics.dm.ua.edu.cdu.appliedMathematics.modeling.spring
     * @param mass
     * @param step - step for calculating
     * @param allSteps - max steps count
     */
    public static void springEuler(final double initialDeviation, final double coeff,
                                   final double mass, final double step, final int allSteps){
        final double angleSpeed = coeff/mass;
        time = new double[allSteps];
        coordinates = new double[allSteps];

        time[0] = 0;
        coordinates[0] = angleSpeed*initialDeviation;

        double tmp = 0;
        for(int i = 0; i < allSteps-1; i++){
            tmp = tmp - step*coordinates[i]*angleSpeed;
            coordinates[i+1] = coordinates[i] + step*tmp;
            time[i+1] = time[i] + step;
        }
    }

    public static double[] getTime() {
        return time;
    }

    public static double[] getCoordinates() {
        return coordinates;
    }
}
