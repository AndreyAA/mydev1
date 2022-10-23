package dev.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryLocality1 {

    public static void main(String[] args) {
        int n = 10_000_000;
        int[] arr = new int[n];
        Integer[] pos = new Integer[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
            pos[i] = i;
        }
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += process(n, arr, pos);
        }
        System.out.println(sum);
    }

    private static long process(int n, int[] arr, Integer[] pos) {
        // shuffle 10M sum 480-540 ms
        // NO shuffle 10M sum 20-33 ms
/*        List<Integer> intList = Arrays.asList(pos);
        Collections.shuffle(intList);
        intList.toArray(pos);*/

        long start = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr[pos[i]];
        }

        long end = System.currentTimeMillis();
        System.out.println(sum + " " + (end - start) + " ms");
        return sum;
    }
}
