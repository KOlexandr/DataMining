package ua.edu.cdu.appliedMathematics.parallel.sorts;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class RankSort {

    private static CountDownLatch latch;
    final static ReentrantLock lock = new ReentrantLock();

    /**
     * Serial sort array using rank sort
     * @param array - array for sort
     * @return new sorted array
     */
    public static int[] sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] sortedArray = new int[array.length];
        for (int anArray1 : array) {
            int x = 0;
            for (int anArray : array) {
                if (anArray1 > anArray) x++;
            }
            while(sortedArray[x] == anArray1) x++;
            sortedArray[x] = anArray1;
        }
        return sortedArray;
    }

    /**
     * Parallel sort array using rank sort
     * @param array - array for sort
     * @param threadCount - number of threads
     * @return new sorted array
     */
    public static int[] sort(final int[] array, final int threadCount){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] sortedArray = new int[array.length];
        latch = new CountDownLatch(threadCount);
        final int elementsForOneThread = countElementForOneThread(threadCount, array.length);
        final Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = initNewThread(elementsForOneThread, i, array, sortedArray);
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sortedArray;
    }

    private static Thread initNewThread(final int elementsForOneThread, final int threadIdx, final int[] array, final int[] sortedArray){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = threadIdx*elementsForOneThread; i < elementsForOneThread*(threadIdx+1) && i < array.length; i++) {
                        int x = 0;
                        for (int anArray : array) {
                            if (array[i] > anArray) x++;
                        }
                        lock.lock();
                            while(sortedArray[x] == array[i]) x++;
                            sortedArray[x] = array[i];
                        lock.unlock();
                    }
                } finally {
                    latch.countDown();
                }
            }
        });
    }

    private static int countElementForOneThread(final int threadCount, final int length){
        final int otherElements = length % threadCount;
        final int tmpLength;
        if(otherElements != 0){
            tmpLength = length + (threadCount-otherElements);
        } else {
            tmpLength = length;
        }
        return tmpLength / threadCount;
    }
}
