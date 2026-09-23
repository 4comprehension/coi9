package com.pivovarit.summaries;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

class VirtualThreadsDemo {

    // 100 threads -> 1010ms
    // 500 threads -> 1028ms
    // 1000 threads -> 1041ms
    // 2000 threads -> 1094ms
    // 5000 threads -> 1468ms

    record Example1() {
        public static void main(String[] args) {
            var e = Executors.newVirtualThreadPerTaskExecutor();

            var threads = 10_000_000;

            timed(() -> {
                List<CompletableFuture<?>> futures = new ArrayList<>();

                for (int i = 0; i < threads; i++) {
                    int finalI = i;
                    CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> process(finalI), e);
                    futures.add(f);
                }

                return futures.stream()
                  .map(CompletableFuture::join)
                  .toList();
            });
        }
    }

    public static <T> T process(T input) {
//        System.out.println("processing " + input + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return input;
    }

    public static <T> T timed(Supplier<T> t) {
        long before = System.nanoTime();
        T result = t.get();
        long after = System.nanoTime();

        System.out.println("duration: " + Duration.ofNanos(after - before).toMillis() + "ms");
        return result;
    }
}
