package ua.edu.cdu.appliedMathematics.parallel.linearSearch;

import ua.edu.cdu.appliedMathematics.parallel.RandomArray;

import java.util.Scanner;

public class LinearSearchTest {

    public static void main(String[] args) {
        int length;
        do{
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Please, input length of array or 0 for exit. length = ");
            length = scanner.nextInt();
            final int[] array = RandomArray.newIntRandomArray(length);
            //final int number = array[RandomArray.getRandomNumber(array.length)];
            array[length-1] = -8;
            final int number = -8;

            long time = System.currentTimeMillis();
            System.out.println("Linear Search Serial:");
            final int sIdx = LinearSearch.search(array, number);
            System.out.println("Number = " + number + ". Serial idx = " + sIdx + ". Found number = " + array[sIdx]);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

            time = System.currentTimeMillis();
            System.out.println("Linear Search Parallel:");
            final int pIdx = LinearSearch.search(array, number, 4);
            System.out.println("Number = " + number + ". Parallel idx = " + pIdx + ". Found number = " + array[pIdx]);
            System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms");

        } while(length > 0);
    }
}
