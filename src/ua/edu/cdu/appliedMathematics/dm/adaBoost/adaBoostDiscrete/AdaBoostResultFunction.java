package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

public abstract class AdaBoostResultFunction {

    /**
     * @param x Point for classification
     * @return -1, 0 or 1
     */
    public abstract int apply(Point x);

    /**
     * find DataClass by result of applying
     * @param x - Point for classification
     * @return - DataClass of given Point
     */
    public DataClass getDataClass(Point x){
        for (DataClass dataClass : DataClass.values()) {
            if(dataClass.getDataClassId() == apply(x)){
                return dataClass;
            }
        }
        throw new RuntimeException("DataClass is not found.");
    }
}