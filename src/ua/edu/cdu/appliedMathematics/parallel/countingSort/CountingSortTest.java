package ua.edu.cdu.appliedMathematics.parallel.countingSort;

import ua.edu.cdu.appliedMathematics.parallel.RandomArray;
import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.bucketSort.BucketSort;

import java.util.Scanner;

public class CountingSortTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            //final int[] array = new int[]{13, 55, 59, 88, 29, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
            final int[] array = RandomArray.newIntRandomArray(length, length/2);

            final int[] serial = new int[length];
            final int[] parallel = new int[length];

            System.arraycopy(array, 0, serial, 0, length);
            System.arraycopy(array, 0, parallel, 0, length);

            long time = System.currentTimeMillis();
            System.out.println("Counting Sort:");
            CountingSort.countingSortSerial(serial);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            Utils.printVector(serial);

            time = System.currentTimeMillis();
            System.out.println("Counting Sort Parallel:");
            CountingSort.countingSortParallel(parallel, 4);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            Utils.printVector(parallel);

            System.out.println("Status = " + Utils.isSameValues(parallel, serial) + "\n");
        } while(length > 0);
    }
}