package ua.edu.cdu.appliedMathematics.parallel;

public class Utils {

    public static final String SEPARATOR = "\t";

    /**
     * verify if array is sorted
     * @param array - maybe sorted array
     * @return boolean status sorted/unsorted
     */
    public static boolean isSorted(final int[] array) {
        for (int i = 0; i < array.length-1; i++) {
            if(array[i] > array[i+1]){
                return false;
            }
        }
        return true;
    }

    /**
     * verify if all items of teo arrays equals one by one
     * @param a - first array
     * @param b - second array
     * @return - status true/false
     */
    public static boolean isSameValues(final int[] a, final int[] b){
        if(null == a || null == b || a.length != b.length){
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * count number of elements from array for one thread
     * @param threadCount - number of all threads
     * @param length - length of array
     * @return - number of elements
     */
    public static int countElementForOneThread(final int threadCount, final int length){
        final int otherElements = length % threadCount;
        final int tmpLength;
        if(otherElements != 0){
            tmpLength = length + (threadCount-otherElements);
        } else {
            tmpLength = length;
        }
        return tmpLength / threadCount;
    }

    public static void printVector(final int[] array) {
        for (int anArray : array) {
            System.out.print(anArray + SEPARATOR);
        }
        System.out.println();
    }

    /**
     * verify if number power of 2
     * @param number - maybe power of 2 (maybe not)
     * @return true if number really power of 2, else return false
     */
    public static boolean isPowerOf2(int number){
        if(number < 1){
            throw new RuntimeException("Number must be positive.");
        }
        while(number%2 != 1){
            number /= 2;
        }
        return number == 1;
    }

    /**
     * find next, for given number, power of 2
     * @param number - given number
     * @return pow of 2
     */
    public static int nextPowerOf2(final int number){
        if(number < 1){
            throw new RuntimeException("Number must be positive.");
        }
        if(isPowerOf2(number)){
            return number;
        }
        int nextNumber = 1;
        while(nextNumber < number){
            nextNumber *= 2;
        }
        return nextNumber;
    }

    /**
     * find prev, for given number, power of 2
     * @param number - given number
     * @return pow of 2
     */
    public static int prevPowerOf2(final int number){
        return nextPowerOf2(number)/2;
    }

    /**
     * find and return max value from integer array
     * @param array - array with data
     * @return max value
     */
    public static int getMax(final int[] array){
        int max = array[0];
        for (int anArray : array) {
            if (max < anArray) {
                max = anArray;
            }
        }
        return max;
    }
}
