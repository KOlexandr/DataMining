package ua.edu.cdu.appliedMathematics.parallel.bitonicSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.RandomArray;

import java.util.Scanner;

public class BitonicSortTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            if(!Utils.isPowerOf2(length)){
                System.out.println("Length must be power of 2. Try again. Next power of 2 = " + Utils.nextPowerOf2(length));
                continue;
            }
            //final int[] array = new int[]{13, 55, 59, 88, 29, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
            final int[] array = RandomArray.newIntRandomArray(length);

            final int[] bitonicArray = new int[length];
            //final int[] bitonicParallelArray = new int[length];

            System.arraycopy(array, 0, bitonicArray, 0, length);
            //System.arraycopy(array, 0, bitonicParallelArray, 0, length);

            long time = System.currentTimeMillis();
            System.out.println("Bitonic Sort:");
            BitonicSort.sort(bitonicArray);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            /*time = System.currentTimeMillis();
            System.out.println("Bitonic Parallel Sort:");
            BitonicSort.sort(bitonicParallelArray);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            Utils.printVector(bitonicParallelArray);*/
            Utils.printVector(bitonicArray);

            System.out.println("Status = " + Utils.isSorted(bitonicArray) + "\n");
        } while(length > 0);
    }
}