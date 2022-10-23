package dev.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncCache {

    private final Map<Long, CompletableFuture<Data>> data = new HashMap<>();
    private AtomicInteger value = new AtomicInteger(0);
    private static final Semaphore sem = new Semaphore(0);

    public void init() {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(3);
        es.scheduleWithFixedDelay(this::update, 5,  5, TimeUnit.SECONDS);
        es.scheduleWithFixedDelay(this::getter, 1, 3, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        new AsyncCache().init();
        //sem.acquire(1);
    }

    public void getter() {
        long start = System.currentTimeMillis();
        CompletableFuture<Data> data = getData(-1);
        Data d = data.join();
        log("got:" + d + " took: " + (System.currentTimeMillis() - start));
    }

    public void update() {
        value.incrementAndGet();
        getCF(-1).thenAccept(d->{
            data.put(-1L, CompletableFuture.completedFuture(d));
            //todo notify();
        });

    }

    public CompletableFuture<Data> getData(long key) {
        return data.computeIfAbsent(key, __ -> getCF(-1));
    }

    private CompletableFuture<Data> getCF(int key) {
        return CompletableFuture.supplyAsync(() -> {
            log("request key: " + key);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int v = value.get();
            log("create new Data: " + v);
            return new Data(v);
        });
    }

    public void log(String str) {
        System.out.println(Thread.currentThread().getName() + " " + str);
    }


    public static class Data {
        private final int value;

        public Data(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "value=" + value +
                    '}';
        }
    }

}
