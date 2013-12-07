package ua.edu.cdu.appliedMathematics.parallel.oddEvenInsertionSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OddEvenInsertionSort {
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
                            insertionSort(array, idx*elementsForOneThread, elementsForOneThread*(idx+2));
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
        //System.out.println("Steps = " + steps);
    }

    /**
     * modified insertion sort for sort parts of array
     * @param array - part of array
     * @param start - start index of array part
     * @param end - end index of array part
     */
    private static void insertionSort(int array[], final int start, int end) {
        end = end > array.length ? array.length : end;
        for (int j = start; j < end; j++) {
            int key = array[j];
            int i = j - 1;
            while (i >= start && array[i] > key) {
                array[i+1] = array[i];
                i--;
            }
            array[i+1] = key;
        }
    }
}
