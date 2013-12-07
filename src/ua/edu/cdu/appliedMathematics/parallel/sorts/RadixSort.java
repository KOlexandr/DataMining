package ua.edu.cdu.appliedMathematics.parallel.sorts;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

public class RadixSort {
    /**
     * sort array using radix sort algorithm
     * @param array - array with data for sort
     */
    public static void sort(final int[] array){
        int pow10 = 1;
        for (int i = 0; i < countRadix(Utils.getMax(array)); i++) {
            countingSort(array, pow10);
            pow10 *= 10;
        }
    }

    /**
     * sort array by one numerical category
     * @param array - original array for sort
     * @param div - number for divide item of array and get needle numerical category
     */
    private static void countingSort(final int[] array, final int div){
        final int items = 10;
        final int[] b = new int[array.length];
        final int[] c = new int[items];
        for (int i = 0; i < b.length; i++) {
            int tmp = (array[i]/div)%10;
            c[tmp]++;
        }
        for (int i = 1; i < items; i++) {
            c[i] += c[i-1];
        }
        for (int i = array.length-1; i >= 0; i--) {
            int tmp = (array[i]/div)%10;
            b[--c[tmp]] = array[i];
        }
        System.arraycopy(b, 0, array, 0, array.length);
    }

    /**
     * count numerical category of number
     * @param number - int number
     * @return - number of numerical category
     */
    private static int countRadix(int number){
        int radix = 0;
        while(number > 9){
            number /= 10;
            radix++;
        }
        return ++radix;
    }
}