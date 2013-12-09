package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.travelingSalesmanProblem;

public class Main {

    public static void main(String[] args) {
        final int cityCount = 10;
        //"makes" cities
        final Point[] cities = makeArrayWithCityCoordinates(cityCount, 100, 100);
        //case first city fo salesman travel (this city finish too)
        final Point startFinish = cities[0];
        //initialize solver of salesman travel problem
        final TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(cities, startFinish);
        System.out.println("Initial data:\n" + tsp.getBetterGeneration());
        int i = 0;
        //run solve while new generation better then current and we have less then 1000 generations
        while (tsp.crossing(10000) && i++ < 1000);
        //gets better generation
        final Generation generation = tsp.getBetterGeneration();
        //print better generation
        System.out.println("\nResult:\n" + generation);
    }

    /**
     * makes array of coordinates of cities, all coordinates unique and random
     * @param cityCount - count of cities (points)
     * @param boundaryOfAreaXMax - max X coordinate
     * @param boundaryOfAreaYMax - max Y coordinate
     * @return - array of points
     */
    private static Point[] makeArrayWithCityCoordinates(final int cityCount, final int boundaryOfAreaXMax, final int boundaryOfAreaYMax){
        final Point[] cityCoordinates= new Point[cityCount];
        for (int i = 0; i < cityCount;) {
            //final Point randomPoint = new Point(tmp[i][0], tmp[i][1]);
            final Point randomPoint = Point.getRandomPoint(boundaryOfAreaXMax, boundaryOfAreaYMax);
            if(!TravelingSalesmanProblem.isExistPoint(cityCoordinates, i, randomPoint)){
                cityCoordinates[i++] = randomPoint;
            }
        }
        return cityCoordinates;
    }

    /* some test data */
    //private static final int[][] tmp = new int[][]{{916,807}, {694,496}, {540,549}, {361,856}, {327,506}, {913,906}, {120,971}, {826,480}, {513,947}, {403,124}};
    //private static final int[][] tmp1 = new int[][]{{1,6}, {10,6}, {4,10}, {7,2}, {2,8}, {7,10}, {2,4}, {9,8}, {4,2}, {9,4}};
}
