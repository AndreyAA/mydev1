package dev.core;

public class MyAsm {

    volatile static int i=0;

    public static void main(String[] args) {

        for (int i=0; i<10000000; i++) {
            calcRes(i);
        }

    }

    private static void calcRes(int value) {
        MyAsm.i = value;
    }
}
