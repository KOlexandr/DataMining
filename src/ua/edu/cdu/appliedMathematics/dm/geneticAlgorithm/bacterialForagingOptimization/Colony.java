package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.bacterialForagingOptimization;

// collection of Bacterium
public class Colony {

    private Bacterium[] bacteria;

    public Colony(int size, int dim, double minValue, double maxValue) {
        this.bacteria = new Bacterium[size];
        for (int i = 0; i < size; ++i){
            // sets position and health but not cost
            bacteria[i] = new Bacterium(dim, minValue, maxValue);
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bacteria.length; ++i){
            sb.append("[").append(i).append("] : ").append(bacteria[i]).append("\n");
        }
        return sb.toString();
    }

    public Bacterium[] getBacteria() {
        return bacteria;
    }

    public void setBacteria(Bacterium[] bacteria) {
        this.bacteria = bacteria;
    }
}
