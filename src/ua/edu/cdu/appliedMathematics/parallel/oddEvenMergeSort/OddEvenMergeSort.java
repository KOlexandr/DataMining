package ua.edu.cdu.appliedMathematics.parallel.oddEvenMergeSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OddEvenMergeSort {
    private static CountDownLatch latch;

    /**
     * Parallel sort array using odd-even sort
     * @param array - array for sort
     * @param threadCount - number of threads
     */
    public static void sort(final int[] array, final int threadCount){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int elementsForOneThread = Utils.countElementForOneThread(threadCount*2, array.length);

        int steps = 0;
        while(!Utils.isSorted(array)){
            final List<Thread> threads = new ArrayList<>();

            for (int i = steps%2, j = 0; i < threadCount*2; i+=2, j++) {
                final int idx = i;
                threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sortMerge(array, idx * elementsForOneThread, elementsForOneThread * (idx + 2));
                        } finally {
                            latch.countDown();
                        }
                    }
                }));
            }
            latch = new CountDownLatch(threads.size());

            for (Thread thread : threads) {
                thread.start();
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            steps++;
        }
        System.out.println("Steps = " + steps);
    }

    /**
     * sort parts of array
     * @param array - array for sort
     * @param p - left index
     * @param r - right index
     */
    private static int partition(final int[] array, final int p, final int r) {
        int x = array[r];
        int i = p - 1;
        for (int j = p; j < r; j++) {
            if(array[j] <= x){
                int tmp = array[++i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
        int tmp = array[++i];
        array[i] = array[r];
        array[r] = tmp;
        return i;
    }

    /**
     * Method sorts array of int
     * @param array - array for sort
     * @param p - start index
     * @param r - end index
     */
    private static void quickSort(final int[] array, final int p, int r) {
        r = r >= array.length ? array.length-1 : r;
        if (p < r) {
            final int q = partition(array, p, r);
            quickSort(array, p, q-1);
            quickSort(array, q + 1, r);
        }
    }

    /**
     * merge parts of array
     * @param array - array for sort
     * @param p - left index
     * @param q = (p + r)/2
     * @param r - right index
     */
    private static void merge(final int[] array, final int p, final int q, final int r) {
        final int lengthLeft = q - p;
        final int lengthRight = r - q;
        final int[] left = new int[lengthLeft+1];
        final int[] right = new int[lengthRight+1];

        System.arraycopy(array, p, left, 0, lengthLeft);
        System.arraycopy(array, q, right, 0, lengthRight);
        left[lengthLeft] = Integer.MAX_VALUE;
        right[lengthRight] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        for (int k = p; k < r; k++){
            if (left[i] <= right[j]){
                array[k] = left[i++];
            } else {
                array[k] = right[j++];
            }
        }
    }

    /**
     * Method sorts array of int
     * @param array - array for sort
     * @param p - start index
     * @param r - end index
     */
    private static void sortMerge(final int[] array, final int p, int r) {
        r = r > array.length ? array.length : r;
        if (p < r) {
            int q = (p + r) / 2;
            //run sortMerge in his thread
            sortMerge(array, p, q);
            //synchronize
            //run sortMerge in his thread
            sortMerge(array, q+1, r);
            //synchronize
            merge(array, p, q, r);
        }
    }
}
