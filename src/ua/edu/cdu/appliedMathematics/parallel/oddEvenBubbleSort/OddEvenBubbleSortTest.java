package ua.edu.cdu.appliedMathematics.parallel.oddEvenBubbleSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.oddEvenSort.OddEvenSort;
import ua.edu.cdu.appliedMathematics.parallel.RandomArray;

import java.util.Scanner;

public class OddEvenBubbleSortTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            //final int[] array = new int[]{13, 55, 59, 88, 29, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
            final int[] array = RandomArray.newIntRandomArray(length);

            final int[] oddEvenArray = new int[length];
            final int[] oddEvenBubbleArray = new int[length];

            System.arraycopy(array, 0, oddEvenArray, 0, length);
            System.arraycopy(array, 0, oddEvenBubbleArray, 0, length);

            long time = System.currentTimeMillis();
            System.out.println("Odd-Even Bubble Sort:");
            OddEvenBubbleSort.sort(oddEvenBubbleArray, 4);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            time = System.currentTimeMillis();
            System.out.println("Odd-Even Sort:");
            OddEvenSort.sort(oddEvenArray);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            System.out.println("Status = " + Utils.isSameValues(oddEvenBubbleArray, oddEvenArray) + "\n");
        } while(length > 0);
    }
}