package ua.edu.cdu.appliedMathematics.dm.geneticAlgorithm.diophantine;

public class Main {

    public static void main(String[] args) {
        final DiophantineSolver diophantineSolver = new DiophantineSolver(new int[]{1,2,3,4}, 30);
        int answer = diophantineSolver.solve();
        if(answer == -1){
            System.out.println("No solution found.");
        } else {
            final Gene g = diophantineSolver.getGene(answer);
            System.out.println("Solution set to a + 2*b + 3*c + 4*d = 30 is:");
            System.out.println("a = " + g.getAlleles()[0]);
            System.out.println("b = " + g.getAlleles()[1]);
            System.out.println("c = " + g.getAlleles()[2]);
            System.out.println("d = " + g.getAlleles()[3]);
        }
    }
}
