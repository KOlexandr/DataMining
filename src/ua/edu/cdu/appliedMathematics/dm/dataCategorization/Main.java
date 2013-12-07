package ua.edu.cdu.appliedMathematics.dm.dataCategorization;

public class Main {
    public static void main(String args[]){
        final double[] rawData = new double[] {
            66, 66, 66, 67, 67, 67, 67, 68, 68, 69,
                    73, 73, 73, 74, 76, 78, 60, 61, 62, 62 };
        final double[] newData = new double[] { 62.0, 66.0, 73.0, 59.5, 75.5, 80.5 };
        final Discretezator<Double> discretezator = new Discretezator<>(rawData);

        System.out.println("=============================================================");
        System.out.println("Raw data:");
        prettyShowVector(rawData, 10);

        System.out.println("Displaying internal structure of the discretizer:");
        System.out.println("-------------------------------------------------------------");
        System.out.println(discretezator.toString());
        System.out.println("-------------------------------------------------------------");

        System.out.println("Generating three existing and three new data values");

        System.out.println("Data values:");
        prettyShowVector(newData, 10);

        System.out.println("Discretizing the data:");
        for (final double data : newData) {
            int cat = discretezator.discretize(data);
            System.out.println(data + " -> " + cat);
        }
        System.out.println("End discretization demo");
        System.out.println("=============================================================");
    }

    /**
     * print some double vector
     * @param data - vector
     * @param columns - count of columns
     */
    private static void prettyShowVector(final double[] data, final int columns) {
        for(int i = 0; i < data.length; i++){
            System.out.print(data[i] + Discretezator.TAB_CHAR);
            if((i+1) % columns == 0){
                System.out.println();
            }
        }
        System.out.println();
    }
}
