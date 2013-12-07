package ua.edu.cdu.appliedMathematics.parallel.linearSearch;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LinearSearch {

    private static CountDownLatch latch;
    private static volatile int index = -1;
    private static volatile boolean isFound;

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

    /**
     * search first item in array which equals input number
     * and return index of this item or -1 if number is not found
     * this function use openMP for parallel algorithm
     * @param array - array with data
     * @param number - number which index will be find
     * @param threadCount - number of threads which will use
     */
    public static int search(final int[] array, final int number, final int threadCount){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        isFound = false;
        final int elementsForOneThread = Utils.countElementForOneThread(threadCount, array.length);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        search(array, number, idx*elementsForOneThread, (idx+1)*elementsForOneThread);
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
        return index;
    }

    /**
     * search given number in subArray
     * @param array - original array
     * @param number - given number
     * @param startIdx - start index of subArray
     * @param endIdx - end index of subArray
     */
    public static void search(final int[] array, final int number, final int startIdx, final int endIdx){
        for(int i = startIdx; i < endIdx && !isFound; i++){
            if(array[i] == number){
                isFound = true;
                index = i;
            }
        }
    }
}
