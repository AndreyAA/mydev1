package dev.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCondition {


    public static class MyQueue {
        private final Lock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();
        private int capacity;
        private int size;
        private final List<Integer> data = new ArrayList<Integer>();

        public void add(Integer value) throws InterruptedException {
            lock.lock();
            try {
                while (capacity == size) {
                    notFull.await();
                }
                data.add(value);
                size++;
            } finally {
                lock.unlock();
            }
        }

        public Integer remove() throws InterruptedException {
            lock.lock();
            try {
                while (size == 0) {
                    notEmpty.await();
                }
                data.remove(0);
                size--;
                notFull.signal();
                
            } finally {
                lock.unlock();
            }
            return null;//todo
        }
    }
}
