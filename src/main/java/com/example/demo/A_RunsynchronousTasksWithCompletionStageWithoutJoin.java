package com.example.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class A_RunsynchronousTasksWithCompletionStageWithoutJoin {
    record Quotation(String server, int amount){}

    public static void main(String[] args) throws InterruptedException {
        run();
    }

    public static void run() throws InterruptedException {
        Random random = new Random();
        Supplier<Quotation> fetchQuotationA =
                () -> {
                    try {
                        Thread.sleep(random.nextInt(80,120));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new Quotation("Server A", random.nextInt(40, 60));
                };

        Supplier<Quotation> fetchQuotationB =
                () -> {
                    try {
                        Thread.sleep(random.nextInt(80,120));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new Quotation("Server B", random.nextInt(30, 70));
                };

        Supplier<Quotation> fetchQuotationC =
                () -> {
                    try {
                        Thread.sleep(random.nextInt(80,120));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new Quotation("Server C", random.nextInt(40, 80));
                };

        var quotationTasks = List.of(fetchQuotationA, fetchQuotationB, fetchQuotationC);
        List<CompletableFuture<Quotation>> futures = new ArrayList<>();
        List<Quotation> quotations = new ArrayList<>();
        Instant begin = Instant.now();
        for(Supplier<Quotation> task: quotationTasks) {
            CompletableFuture<Quotation> future = CompletableFuture.supplyAsync(task);
            futures.add(future);
        }
        List<CompletableFuture<Void>> voids = new ArrayList<>();
        for(CompletableFuture<Quotation> future: futures) {
            CompletableFuture<Void> accept = future.thenAccept(System.out::println);
            voids.add(accept);
            future.thenAccept(quotations::add);

        }
        for (CompletableFuture<Void> v : voids) {
            v.join();
        }
        System.out.println(quotations);
        // Thread.sleep is not a good way
     //   Thread.sleep(500);


//        Quotation bestQuotation = quotations.stream()
//                                            .min(Comparator.comparing(Quotation::amount)).orElseThrow();
//        Instant end = Instant.now();
//        Duration duration = Duration.between(begin, end);
//        System.out.println("Best quotation [Sync] = " + bestQuotation + " ( " + duration.toMillis() + " ms)");
    }
}
