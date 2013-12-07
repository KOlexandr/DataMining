package ua.edu.cdu.appliedMathematics.parallel.sorts;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.bucketSort.BucketSort;

public class Main {

    public static void main(String args[]){
        final int[] array = new int[]{13, 55, 59, 88, 209, 41, 71, 85, 2, 18, 40, 75, 4, 4, 4, 43};
        //final int[] array = RandomArray.newIntRandomArray(1000);
        //final int[] sorted = QuickSort.sort(array);
        //System.out.println("Original array:");
        new BucketSort().sortConcurrent(array, 4);
        Utils.printVector(array);
    }
}