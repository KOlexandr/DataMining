package ua.edu.cdu.appliedMathematics.parallel.binarySearch;

import ua.edu.cdu.appliedMathematics.parallel.Utils;
import ua.edu.cdu.appliedMathematics.parallel.sorts.CountingSort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BinarySearch {

    private static CountDownLatch latch;
    private static volatile boolean isFound;
    public static final int KEY_NOT_FOUND = -1;
    private static volatile int index = KEY_NOT_FOUND;

    /**
     * serial iterative binary search. at first sort array with counting sort
     * and then run serial iterative binary search
     * @param array - original array with data
     * @param key - number which need to find in array
     * @return index of found key or -1 if key not found
     */
    public static int sortAndSearch(final int[] array, final int key){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        CountingSort.sortArray(array);
        return search(array, key);
    }

    /**
     * serial iterative binary search
     * use this method if you sure that your array is sorted
     * @param array - original array with data
     * @param key - number which need to find in array
     * @return index of found key or -1 if key not found
     */
    public static int search(final int[] array, final int key){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        int start = 0;
        int end = array.length-1;
        int middle = (end-start)/2;
        while (start <= end) {
            if (key == array[middle]) {
                return middle;
            } else if (array[middle] < key) {
                start = middle + 1;
            } else {
                end = middle - 1;
            }
            middle = (start + end) / 2;
        }
        return KEY_NOT_FOUND;
    }

    /**
     * recursive binary search for subArray
     * @param array - original array with data
     * @param key - number which need to find in array
     * @param startIdx - start index of subArray
     * @param endIdx - end index of subArray
     * @return index of found key or -1 if key not found
     */
    public static int searchRecursive(final int array[], final int key, final int startIdx, final int endIdx) {
        if (endIdx < startIdx){
            return KEY_NOT_FOUND;
        } else {
            int iMid = startIdx + ((endIdx - startIdx) / 2);
            if (array[iMid] > key) {
                return searchRecursive(array, key, startIdx, iMid-1);
            } else if (array[iMid] < key) {
                return searchRecursive(array, key, iMid+1, endIdx);
            } else {
                return iMid;
            }
        }
    }

    /**
     * parallel sort and search. at first sort array with counting sort and run parallel binary search
     * @param array - original array with data
     * @param key - number which need to find in array
     * @param threadCount number of threads
     * @return index of found key or -1 if key not found
     */
    public static int sortAndSearch(final int[] array, final int key, final int threadCount){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        CountingSort.sortArray(array);
        return search(array, key, threadCount);
    }

    /**
     * parallel version of binary search. This version works not very quickly because time of work
     * of serial version is log(n) and paralleling this algorithm is bad idea.
     * For small count of elements parallel version will work slower (or very slower)
     * use this method if you sure that your array is sorted
     * @param array - original array with data
     * @param key - number which need to find in array
     * @param threadCount number of threads
     * @return index of found key or -1 if key not found
     */
    public static int search(final int[] array, final int key, final int threadCount){
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
                        search(array, key, idx*elementsForOneThread, (idx+1)*elementsForOneThread);
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
     * iterative binary search for subArray
     * @param array - original array with data
     * @param key - number which need to find in array
     * @param startIdx - start index of subArray
     * @param endIdx - end index of subArray
     */
    private static void search(final int[] array, final int key, final int startIdx, final int endIdx){
        int start = startIdx;
        int end = endIdx > array.length ? array.length-1 : endIdx;
        int middle = (end-start)/2;
        while (start <= end && !isFound && middle < array.length) {
            if (key == array[middle]) {
                isFound = true;
                index = middle;
            } else if (array[middle] < key) {
                start = middle + 1;
            } else {
                end = middle - 1;
            }
            middle = (start + end) / 2;
        }
    }
}
