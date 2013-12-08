package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostDiscrete;

public enum DataClass {
    MINUS(-1), ZERO(0), PLUS(1);

    private final int dataClassId;

    DataClass(int dataClassId) {
        this.dataClassId = dataClassId;
    }

    public int getDataClassId() {
        return dataClassId;
    }

    @Override
    public String toString() {
        return name() + "(" + dataClassId + ")";
    }
}
