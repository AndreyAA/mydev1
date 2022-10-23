package dev.perf;


import org.openjdk.jmh.annotations.*;

import java.io.IOException;

public class TestVolatile {
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    /**
     * throughput
     * final 1 463 754 085
     * plain   144 872 517
     * volatile 26 575 641
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    public int test(Storage s) {
        int sum = 0;
        for (int i = 0; i < s.v; i++) {
            sum += s.v;
        }
        return sum;
    }

    @State(Scope.Benchmark)
    public static class Storage {
        final int v = 42;
    }

}


