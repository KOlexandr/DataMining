package ua.edu.cdu.appliedMathematics.dm.adaBoost.adaBoostFromSharp;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LinearSearch {

    /**
     * search first item in array which equals input number
     * and return index of this item or -1 if number is not found
     * @param array - array with data
     * @param number - number which index will be find
     */
    public static int search(final int[] array, final int number){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        for(int i = 0; i < array.length; i++){
            if(array[i] == number){
                return i;
            }
        }
        return -1;
    }
}
