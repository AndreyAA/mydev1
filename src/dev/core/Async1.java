package dev.core;

import java.util.concurrent.*;

public class Async1 {

    public static void main(String[] args) {

        CompletableFuture<Integer> cf1 = new CompletableFuture<Integer>();
        ExecutorService es = Executors.newFixedThreadPool(1);

        CompletableFuture<Integer> cf2 = cf1.handleAsync((val,ex)->{
           throw new IllegalStateException("ex");
        });

        es.submit(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cf1.complete(5);
        });

        try {
            Integer res = cf2.join();
            System.out.println("res: " + res);

        } finally {
            System.out.println("end: ");
        }

    }
}
