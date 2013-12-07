package ua.edu.cdu.appliedMathematics.parallel.sorts;

public class ShellSort {
    /**
     * Sort array using shell sort
     * @param array - array for sort
     * @return new sorted array
     */
    public static int[] sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] gaps = new int[]{701, 301, 132, 57, 23, 10, 4, 1};
        final int[] sortedArray = new int[array.length];
        System.arraycopy(array, 0, sortedArray, 0, array.length);
        for (int gap: gaps) {
            for (int i = gap; i < sortedArray.length; i++) {
                int tmp = sortedArray[i], j;
                for (j = i; j >= gap && sortedArray[j - gap] > tmp; j -= gap) {
                    sortedArray[j] = sortedArray[j - gap];
                }
                sortedArray[j] = tmp;
            }
        }
        return sortedArray;
    }
}
