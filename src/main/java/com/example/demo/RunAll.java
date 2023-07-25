package com.example.demo;

import java.util.concurrent.ExecutionException;

public class RunAll {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        A_RunsynchronousTasks.run();
        A_RunsynchronousTasksWithCompletionStage.run();
        A_RunsynchronousTasksWithExecutorService.run();
    }
}
