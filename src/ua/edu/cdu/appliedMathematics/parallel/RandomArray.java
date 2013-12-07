package ua.edu.cdu.appliedMathematics.parallel;

import java.util.Random;

public class RandomArray {
    public static double[] newDoubleRandomArray(final int length){
        return newDoubleRandomArray(length, 1024);
    }

    public static double[] newDoubleRandomArray(final int length, final int maxValue){
        final Random random = new Random();
        final double[] array = new double[length];
        for(int i = 0; i < length; i++){
            array[i] = random.nextInt(maxValue);
        }
        return array;
    }

    public static int[] newIntRandomArray(final int length){
        return newIntRandomArray(length, 1024);
    }

    public static int[] newIntRandomArray(final int length, final int maxValue){
        final Random random = new Random();
        final int[] array = new int[length];
        for(int i = 0; i < length; i++){
            array[i] = random.nextInt(maxValue)+1;
        }
        return array;
    }

    public static int getRandomNumber(final int num){
        return new Random().nextInt(num);
    }
}