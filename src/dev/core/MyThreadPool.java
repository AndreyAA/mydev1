package dev.core;

import org.junit.Test;

import java.util.concurrent.*;

public class MyThreadPool {


    @Test
    public void test1() throws InterruptedException, ExecutionException, TimeoutException {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor ex = new ThreadPoolExecutor(2,10, 5, TimeUnit.SECONDS, queue);

        for (int i=0;i<20; i++) {
//            System.out.println(i);
            ex.execute(wrap(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "thread " + i));
        }
        CompletableFuture<Void> cf = CompletableFuture.runAsync(()->{
            while (true) {
                System.out.println("active threads: " + ex.getActiveCount() + ", pool size:" + ex.getPoolSize());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        System.out.println("active threads: " + ex.getActiveCount());
        Thread.sleep(7000);
        System.out.println("active threads: " + ex.getActiveCount());

        System.out.println("last 20 secs");
        ex.awaitTermination(20, TimeUnit.SECONDS);
        System.out.println("cf get");
        cf.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void test2() throws InterruptedException, ExecutionException, TimeoutException {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(11);
        ThreadPoolExecutor ex = new ThreadPoolExecutor(2,10, 5, TimeUnit.SECONDS, queue);

        for (int i=0;i<20; i++) {
//            System.out.println(i);
            ex.execute(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            /*
            ex.execute(wrap(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "task " + i));

             */
        }
        ex.execute(()->{
            System.out.println("abc");
        });
        System.out.println("active threads: " + ex.getActiveCount() + ", pool size:" + ex.getPoolSize());
        System.out.println("active threads: " + ex.getActiveCount() + ", pool size:" + ex.getPoolSize());

    }

    @Test
    public void test3() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(11);
        ThreadPoolExecutor ex = new ThreadPoolExecutor(1,1, 5, TimeUnit.SECONDS, queue);

        CompletableFuture<Void> cf = CompletableFuture.runAsync(wrap(()->{
            sleep();
            System.out.println();
        }, "1"), ex).thenRunAsync(
                wrap(()->{
                    sleep();
                    System.out.println();
                }, "2")
                , ex).thenRunAsync(
                wrap(()->{
                    sleep();
                    System.out.println();
                }, "3")
                , ex).thenRunAsync(
                wrap(()->{
                    sleep();
                    System.out.println();
                }, "4"), ex
        );

        System.out.println("active threads: " + ex.getActiveCount() + ", pool size:" + ex.getPoolSize());
        System.out.println("active threads: " + ex.getActiveCount() + ", pool size:" + ex.getPoolSize());
    }

    private static void sleep()  {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static NamedRunnable wrap(Runnable r, String name) {
        long addTime = System.currentTimeMillis();
        return new NamedRunnable() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                r.run();
                long finishTime = System.currentTimeMillis();
                System.out.println("wait: " + (startTime-addTime) + " ms, runTime: " + (finishTime-startTime) + " ms");
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    public interface NamedRunnable extends Runnable {
        public String name();
    }
}
