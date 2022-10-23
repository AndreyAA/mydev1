package dev.perf;

import java.io.IOException;

public class Singleton {

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    public int test(TestVolatile.Storage s) {
        int sum = 0;
        for (int i = 0; i < s.v; i++) {
            sum += s.v;
        }
        return sum;
    }

    public static class MySingleton {
        private static MySingleton instance;

        private MySingleton() {

        }

        public static MySingleton createInstance() {
            synchronized (MySingleton.class) {
                if (instance == null) {
                    instance = new MySingleton();
                }
                return instance;
            }
        }

    }
}
