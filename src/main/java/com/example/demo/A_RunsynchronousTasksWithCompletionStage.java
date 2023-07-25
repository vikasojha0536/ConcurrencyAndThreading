package com.example.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class A_RunsynchronousTasksWithCompletionStage {
    record Quotation(String server, int amount){}

    public static void main(String[] args) {
        run();
    }

    public static void run() {
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
                    return new Quotation("Server A", random.nextInt(30, 70));
                };

        Supplier<Quotation> fetchQuotationC =
                () -> {
                    try {
                        Thread.sleep(random.nextInt(80,120));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new Quotation("Server A", random.nextInt(40, 80));
                };

        var quotationTasks = List.of(fetchQuotationA, fetchQuotationB, fetchQuotationC);
        List<CompletableFuture<Quotation>> futures = new ArrayList<>();
        List<Quotation> quotations = new ArrayList<>();
        Instant begin = Instant.now();
        for(Supplier<Quotation> task: quotationTasks) {
            CompletableFuture<Quotation> future = CompletableFuture.supplyAsync(task);
            futures.add(future);
        }
        for(CompletableFuture<Quotation> future: futures) {
            Quotation quotation = future.join();
            quotations.add(quotation);
        }
        Quotation bestQuotation = quotations.stream()
                                            .min(Comparator.comparing(Quotation::amount)).orElseThrow();
        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        System.out.println("Best quotation [Sync] = " + bestQuotation + " ( " + duration.toMillis() + " ms)");
    }

}
