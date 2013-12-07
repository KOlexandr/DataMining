package ua.edu.cdu.appliedMathematics.parallel.binarySearch;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.RandomArray;

import java.util.Scanner;

public class BinarySearchTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            final int[] array = RandomArray.newIntRandomArray(length);
            //final int number = array[RandomArray.getRandomNumber(array.length)];
            /**
             * the longest time for finding number need for element with index = 2^n-1 or 2^n+1
             */
            final int idx = Utils.prevPowerOf2(length) - 1;
            final int number = 2048;
            array[idx] = number;
            for(int i = idx+1; i < array.length; i++){
                array[i] = number+1;
            }

            long time = System.currentTimeMillis();
            System.out.println("Binary Search Serial:");
            final int sIdx = BinarySearch.sortAndSearch(array, number);
            if(BinarySearch.KEY_NOT_FOUND == sIdx){
                System.out.println("Number = " + number + " is not found.");
            } else {
                System.out.println("Number = " + number + ". Serial idx = " + sIdx + ". Found number = " + array[sIdx]);
            }
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            time = System.currentTimeMillis();
            System.out.println("Binary Search Parallel:");
            final int pIdx = BinarySearch.sortAndSearch(array, number, 4);
            if(BinarySearch.KEY_NOT_FOUND == sIdx){
                System.out.println("Number = " + number + " is not found.");
            } else {
                System.out.println("Number = " + number + ". Parallel idx = " + pIdx + ". Found number = " + array[pIdx]);
            }
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

        } while(length > 0);
    }
}
