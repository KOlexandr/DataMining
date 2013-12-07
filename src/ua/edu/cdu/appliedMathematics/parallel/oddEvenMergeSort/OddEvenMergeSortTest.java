package ua.edu.cdu.appliedMathematics.parallel.oddEvenMergeSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.oddEvenSort.OddEvenSort;
import ua.edu.cdu.appliedMathematics.parallel.RandomArray;

import java.util.Scanner;

public class OddEvenMergeSortTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            //final int[] array = new int[]{13, 55, 59, 88, 29, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
            final int[] array = RandomArray.newIntRandomArray(length);

            final int[] oddEvenArray = new int[length];
            final int[] oddEvenMergeArray = new int[length];

            System.arraycopy(array, 0, oddEvenArray, 0, length);
            System.arraycopy(array, 0, oddEvenMergeArray, 0, length);

            long time = System.currentTimeMillis();
            System.out.println("Odd-Even Merge Sort:");
            OddEvenMergeSort.sort(oddEvenMergeArray, 4);
            //Utils.printVector(oddEvenMergeArray);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            time = System.currentTimeMillis();
            System.out.println("Odd-Even Sort:");
            OddEvenSort.sort(oddEvenArray);
            //Utils.printVector(oddEvenArray);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            System.out.println("Status = " + Utils.isSameValues(oddEvenMergeArray, oddEvenArray) + "\n");
        } while(length > 0);
    }
}