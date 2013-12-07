package ua.edu.cdu.appliedMathematics.parallel.countingSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * prefix sum not work
 */

public class CountingSort {

    private static CyclicBarrier barrier;

    /**
     * find max value in array and start countingSort, as result sort array
     * run this method if you do not know max element in array
     * @param array - original unsorted array
     */
    public static void countingSortSerial(final int[] array){
        //countingSortSerial(array, Utils.getMax(array));
        countingSortSerialUnstable(array, Utils.getMax(array));
    }

    /**
     * sort array using counting sort algorithm
     * run this method if you know max element in array
     * @param array - original unsorted array
     * @param k - max value in array
     */
    public static void countingSortSerial(final int[] array, final int k){
        final int length = array.length;
        final int[] b = new int[length];
        final int[] c = new int[k+1];
        for (int i = 0; i < k+1; ++i){
            c[i] = 0;
        }
        for (int anArray : array) {
            c[anArray]++;
        }
        for (int i = 1; i <= k; i++) {
            c[i] += c[i-1];
        }
        for (int i = length-1; i >= 0; i--) {
            b[--c[array[i]]] = array[i];
        }
        System.arraycopy(b, 0, array, 0, length);
    }

    /**
     * sort array using counting sort algorithm (not stable)
     * run this method if you know max element in array
     * @param array - original unsorted array
     * @param k - max value in array
     */
    public static void countingSortSerialUnstable(final int[] array, final int k){
        final int[] counts = new int[k+1];
        for (int i = 0; i < k+1; ++i){
            counts[i] = 0;
        }

        for (int anArray : array) {
            counts[anArray]++;
        }

        for (int i = 1; i < k+1; ++i){
            counts[i] = counts[i] + counts[i-1];
        }

        for (int v = 0; v < k; ++v) {
            for (int i = counts[v]; i < counts[v+1]; ++i){
                array[i] = v;
            }
        }
    }

    /**
     * find max value in array and start countingSortParallel, as result sort array
     * run this method if you do not know max element in array
     * @param array - original unsorted array
     * @param numThreads - number of threads
     */
    public static void countingSortParallel(final int[] array, final int numThreads){
        //countingSortParallel(array, Utils.getMax(array), numThreads);
        countingSortParallelUnstable(array, Utils.getMax(array), numThreads);
    }

    /**
     * sort array using parallel counting sort algorithm
     * run this method if you know max element in array
     * @param array - original unsorted array
     * @param k - max value in array
     * @param numThreads - number of threads
     */
    public static void countingSortParallel(final int[] array, final int k, final int numThreads){
        final int length = array.length;
        final int[] b = new int[length];
        final int[] c = new int[k+1];

        //#pragma omp parallel for
        for (int i = 0; i < k+1; ++i){
            c[i] = 0;
        }

        for (int anArray : array) {
            c[anArray]++;
        }

        prefixSum(c, k+1, numThreads);

        for (int i = length-1; i >= 0; i--) {
            b[--c[array[i]]] = array[i];
        }

        System.arraycopy(b, 0, array, 0, length);
    }

    /**
     * sort array using parallel counting sort algorithm (not stable)
     * run this method if you know max element in array
     * @param array - original unsorted array
     * @param k - max value in array
     * @param numThreads - number of threads
     */
    public static void countingSortParallelUnstable(final int[] array, final int k, final int numThreads){
        final int[] counts = new int[k+2];

        // #pragma omp parallel for
        for (int i = 0; i < k+2; ++i){
            counts[i] = 0;
        }

        for (int anArray : array) {
            counts[anArray + 1]++;
        }

        prefixSum(counts, k+2, numThreads);

        //#pragma omp parallel for
        for (int v = 0; v < k; ++v) {
            for (int i = counts[v]; i < counts[v+1]; ++i){
                array[i] = v;
            }
        }
    }

    /**
     * count prefix sum of array using openMP
     * @param c - original and result array
     * @param numThreads - number of threads
     */
    private static void prefixSum(final int[] c, final int k, final int numThreads){
        final int[] z = new int[k];
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int idx = i;
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final int work = (k + numThreads-1) / numThreads;
                        final int lo = work * idx;
                        int hi = lo + work;
                        if (hi > k){
                            hi = k;
                        }
                        for (int i = lo+1; i < hi; i++){
                            c[i] = c[i] + c[i-1];
                        }
                        z[idx] = c[hi-1];

                        barrier.await();
                        for (int j = 1; j < numThreads; j = 2*j) {
                            if (idx >= j){
                                z[idx] = z[idx] + z[idx - j];
                            }
                            barrier.await();
                        }
                        for (int i = lo; i < hi; i++){
                            c[i] = c[i] + z[idx] - c[hi-1];
                        }
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        barrier = new CyclicBarrier(threads.size());
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
