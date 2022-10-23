package dev.core;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MyVolatile {

    @Test
    public void test() throws InterruptedException {
        Volatile v = new Volatile();
        Thread th1 = new Thread(()->v.writer());
        Thread th2 = new Thread(()->v.reader());
        th2.start();
        th1.start();

        th1.join();
        th2.join();
        assertFalse(v.error);
    }

    public static class Volatile {
        private int x;
        private int y;
        private volatile boolean run;
        private volatile boolean error;

        public void writer() {
            x = 42;
            run = true;
            y = 35;
        }

        public void reader() {
            while(!run) {}
            try {
                assertEquals(42, x);
//                assertEquals(35, y);
            } catch (Error ex) {
                error=true;
            }
//            System.out.println("assert ok");
        }
    }

}
