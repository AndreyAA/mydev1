package dev.core;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ComputeIfAbsent {

    private Map<Key, CompletableFuture<Integer>> data = new HashMap<>();

    public CompletableFuture<Integer> load(List<Key> ccys) {
        CompletableFuture<Integer>[] list = new CompletableFuture[ccys.size()];
        for (int i = 0; i < ccys.size(); i++) {
            list[i] = getDataForKey(ccys.get(i));
        }

        CompletableFuture<Integer> res = CompletableFuture.allOf(list).thenApply(V -> {
            return Arrays.stream(list).mapToInt(CompletableFuture::join).sum();
        });

        return res;
    }

    private CompletableFuture<Integer> getDataForKey(Key key) {
        return data.computeIfAbsent(key, key1 -> {
            System.out.println("request: " + key1);
            return CompletableFuture.completedFuture(key1.getCcy());
        });
    }

    @Test
    public void test1() {
        data.put(new Key(1), CompletableFuture.completedFuture(1));

        List<Key> request = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            request.add(new Key(i));
        }

        System.out.println("step1");
        CompletableFuture<Integer> res = load(request);
        System.out.println(res.join());

        System.out.println("step2");
        request.add(new Key(10));
        CompletableFuture<Integer> res2 = load(request);
        System.out.println(res2.join());

    }

    public static class Key {
        private final Integer ccy;

        public Key(Integer ccy) {
            this.ccy = ccy;
        }

        public Integer getCcy() {
            return ccy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return ccy.equals(key.ccy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ccy);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "ccy=" + ccy +
                    '}';
        }
    }
}
