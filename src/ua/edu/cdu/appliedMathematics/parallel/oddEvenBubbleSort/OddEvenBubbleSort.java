package ua.edu.cdu.appliedMathematics.parallel.oddEvenBubbleSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OddEvenBubbleSort {
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
                            bubbleSort(array, idx*elementsForOneThread, elementsForOneThread*(idx+2));
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
     * Sort array using merge sort
     * @param array - array for sort
     * @param start - start index of part of array
     * @param end - end index of part of array
     */
    private static void bubbleSort(final int[] array, final int start, int end){
        end = end > array.length ? array.length : end;
        for(int i = end-1; i >= start; i--){
            for(int j = start; j < i; j++){
                if(array[j] > array[j+1]){
                    int tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
    }
}
