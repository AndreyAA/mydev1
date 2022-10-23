package dev.core;

public class MyEpsilonGC {

    public static void main(String[] args) {
        System.out.println("hi");
        for (int i=0;i<100; i++) {
            System.out.println(Runtime.getRuntime().freeMemory());
            int[] v = new int[100000];
        }
        System.out.println(Runtime.getRuntime().freeMemory());
    }
}
