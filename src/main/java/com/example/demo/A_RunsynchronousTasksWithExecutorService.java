package com.example.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class A_RunsynchronousTasksWithExecutorService {
    record Quotation(String server, int amount){}

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        run();
    }

    public static void run() throws ExecutionException, InterruptedException {
        Random random = new Random();
        Callable<Quotation> fetchQuotationA =
                () -> {
                 Thread.sleep(random.nextInt(80,120));
                 return new Quotation("Server A", random.nextInt(40, 60));
                };

        Callable<Quotation> fetchQuotationB =
                () -> {
                    Thread.sleep(random.nextInt(80,120));
                    return new Quotation("Server A", random.nextInt(30, 70));
                };

        Callable<Quotation> fetchQuotationC =
                () -> {
                    Thread.sleep(random.nextInt(80,120));
                    return new Quotation("Server A", random.nextInt(40, 80));
                };

        var quotationTasks = List.of(fetchQuotationA, fetchQuotationB, fetchQuotationC);
        var executor = Executors.newFixedThreadPool(4);
        List<Future<Quotation>> futures = new ArrayList<>();
        List<Quotation> quotations = new ArrayList<>();
        Instant begin = Instant.now();
        for(Callable<Quotation> task: quotationTasks) {
            Future<Quotation> future = executor.submit(task);
            futures.add(future);
        }
        for(Future<Quotation> future: futures) {
            Quotation quotation = future.get();
            quotations.add(quotation);
        }
        Quotation bestQuotation = quotations.stream()
                                        .min(Comparator.comparing(Quotation::amount)).orElseThrow();
        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        System.out.println("Best quotation [Sync] = " + bestQuotation + " ( " + duration.toMillis() + " ms)");
        executor.shutdown();



    }

    private static Quotation fetchQuotation(Callable<Quotation> t) {
        try {
            return t.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
