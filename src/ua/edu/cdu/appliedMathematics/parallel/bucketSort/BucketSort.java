package ua.edu.cdu.appliedMathematics.parallel.bucketSort;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * parallel version work not correct
 */
public class BucketSort {

    private CountDownLatch latch;
    
    public void sortConcurrent(final int[] array, final int threadCount){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int length = array.length;
        final int pow = countPowOf10(Utils.getMax(array));
        final double[] dArray = new double[length];
        final ConcurrentMap<Integer, CopyOnWriteArrayList<Double>> map = new ConcurrentHashMap<>();
        final int elementsForOneThread = Utils.countElementForOneThread(threadCount, array.length);

        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformAndFillBuckets(dArray, array, pow, idx * elementsForOneThread, (idx + 1) * elementsForOneThread, map);
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

        for (Map.Entry<Integer, CopyOnWriteArrayList<Double>> entry : map.entrySet()) {
            insertionSort(entry.getValue());
        }
        
        int j = 0;
        for (Map.Entry<Integer, CopyOnWriteArrayList<Double>> entry : map.entrySet()) {
            for (Double aDouble : entry.getValue()) {
                dArray[j++] = aDouble;
            }
        }

        threads.clear();
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformToIntArray(array, dArray, pow, idx * elementsForOneThread, (idx + 1) * elementsForOneThread);
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
    }

    private void transformAndFillBuckets(final double[] dArray, final int[] array, final int pow, final int start,
                                                int end, final ConcurrentMap<Integer, CopyOnWriteArrayList<Double>> map){
        final int length = array.length;
        end = end > length ? length : end;
        for (int i = start; i < end; i++) {
            dArray[i] = array[i]/(1.0 * pow);
        }
        for (int i = start; i < end; i++) {
            final int tmp = (int) (dArray[i] * length);
            if(null == map.get(tmp)){
                synchronized (this){
                    if(null == map.get(tmp)){
                        map.put(tmp, new CopyOnWriteArrayList<Double>());
                    }
                }
            }
            synchronized (this){
                map.get(tmp).add(dArray[i]);
            }
        }
    }

    private static void transformToIntArray(final int[] array, final double[] dArray, final int pow, final int start, int end){
        final int length = array.length;
        end = end > length ? length : end;
        for (int i = start; i < end; i++) {
            array[i] = (int)(dArray[i]*pow);
        }
    }
    
    public static void sort(final int[] array){
        final int length = array.length;
        final int pow = countPowOf10(Utils.getMax(array));
        final double[] dArray = new double[length];
        for (int i = 0; i < length; i++) {
            dArray[i] = array[i]/(1.0 * pow);
        }
        final Map<Integer, List<Double>> map = new HashMap<>();
        for (int i = 0 ; i < length; i++) {
            final int tmp = (int) (dArray[i] * length);
            if(null == map.get(tmp)){
                map.put(tmp, new ArrayList<Double>());
            }
            map.get(tmp).add(dArray[i]);
        }
        for (Map.Entry<Integer, List<Double>> entry : map.entrySet()) {
            insertionSort(entry.getValue());
        }
        int j = 0;
        for (Map.Entry<Integer, List<Double>> entry : map.entrySet()) {
            for (Double aDouble : entry.getValue()) {
                dArray[j++] = aDouble;
            }
        }
        for (int i = 0; i < length; i++) {
            array[i] = (int)(dArray[i]*pow);
        }
    }
    
    /**
     * modified insertion sort for sort concurrent list
     */
    private static void insertionSort(List<Double> list) {
        for (int j = 0; j < list.size(); j++) {
            double key = list.get(j);
            int i = j - 1;
            while (i >= 0 && list.get(i) > key) {
                list.set(i+1, list.get(i));
                i--;
            }
            list.set(i+1, key);
        }
    }

    /**
     * count numerical category of number
     * @param number - int number
     * @return - number of numerical category
     */
    private static int countPowOf10(int number){
        int powOf10 = 1;
        while(number > 9){
            number /= 10;
            powOf10 *= 10;
        }
        return powOf10 *= 10;
    }
}