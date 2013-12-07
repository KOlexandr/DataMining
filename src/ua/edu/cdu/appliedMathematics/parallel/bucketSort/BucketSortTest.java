package ua.edu.cdu.appliedMathematics.parallel.bucketSort;

import ua.edu.cdu.appliedMathematics.parallel.RandomArray;
import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.Scanner;

public class BucketSortTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            //final int[] array = new int[]{13, 55, 59, 88, 29, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
            final int[] array = RandomArray.newIntRandomArray(length);

            final int[] serial = new int[length];
            final int[] parallel = new int[length];

            System.arraycopy(array, 0, serial, 0, length);
            System.arraycopy(array, 0, parallel, 0, length);

            long time = System.currentTimeMillis();
            System.out.println("Bucket Sort:");
            BucketSort.sort(serial);

            //Utils.printVector(serial);

            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            time = System.currentTimeMillis();
            System.out.println("Bucket Sort Parallel:");
            new BucketSort().sortConcurrent(parallel, 2);

            //Utils.printVector(parallel);

            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            System.out.println("Status = " + Utils.isSameValues(parallel, serial) + "\n");
        } while(length > 0);
    }
}