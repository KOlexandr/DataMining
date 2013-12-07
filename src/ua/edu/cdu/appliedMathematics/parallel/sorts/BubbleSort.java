package ua.edu.cdu.appliedMathematics.parallel.sorts;

public class BubbleSort {
    /**
     * Sort array using merge sort
     * @param array - array for sort
     * @return new sorted array
     */
    public static int[] sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] sortedArray = new int[array.length];
        System.arraycopy(array, 0, sortedArray, 0, array.length);
        for(int i = sortedArray.length-1; i >= 0; i--){
            for(int j = 0; j < i; j++){
                if(sortedArray[j] > sortedArray[j+1]){
                    int tmp = sortedArray[j];
                    sortedArray[j] = sortedArray[j+1];
                    sortedArray[j+1] = tmp;
                }
            }
        }
        return sortedArray;
    }
}
