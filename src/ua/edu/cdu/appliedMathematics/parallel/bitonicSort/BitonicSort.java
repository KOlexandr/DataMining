package ua.edu.cdu.appliedMathematics.parallel.bitonicSort;

public class BitonicSort {

    /**
     * calls bitonicSort for array with data and use it sort array
     * @param array - array with data
     */
    public static void sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        sorter(array, 0, array.length-1);
    }

    /**
     * calls sorter for subArrays of current subArray of input array and merge sorted subArrays
     * @param array - array with data
     * @param p - start of subArray
     * @param r - end of subArray
     */
    private static void sorter(final int[] array, final int p, final int r){
        final int q = (p+r)/2;
        if(p < q){
            sorter(array, p, q);
        }
        if(q+1 < r){
            sorter(array, q+1, r);
        }
        merger(array, p ,r);
    }

    /**
     * makes reverse of subArray and calls bitonicSorter for subArrays of current subArray
     * @param array - array with data
     * @param p - start of subArray
     * @param r - end of subArray
     */
    private static void merger(final int[] array, final int p, final int r){
        int i = p;
        int j = r;
        while(i < j){
            comparator(array, i, j);
            i++; j--;
        }
        final int q = (p + r)/2;
        bitonicSorter(array, p, q);
        bitonicSorter(array, q+1, r);
    }

    /**
     * makes halfClean of subArray (recursive calls itself for halfClean smaller subArrays of input subArray)
     * @param array - array with data
     * @param p - start of subArray
     * @param r - end of subArray
     */
    private static void bitonicSorter(final int[] array, final int p, final int r){
        final int q = (p+r)/2;
        if(p < r){
            halfCleaner(array, p, r);
            bitonicSorter(array, p, q);
            bitonicSorter(array, q+1, r);
        }
    }

    /**
     * makes bitonic sequence
     * @param array - array with data
     * @param p - start of subArray
     * @param r - end of subArray
     */
    private static void halfCleaner(final int[] array, final int p, final int r){
        final int q = (p+r)/2;
        for (int i = 0; i < (r-p+1)/2; ++i){
            comparator(array, p+i, q+1+i);
        }
    }

    /**
     * compare values with ids in array and swap them if first > second
     * @param array - array with values
     * @param i - first index
     * @param j - second index
     */
    private static void comparator(final int[] array, final int i, final int j){
        if(array[i] > array[j]){
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
}