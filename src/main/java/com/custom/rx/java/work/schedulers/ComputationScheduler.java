package com.custom.rx.java.work.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Планировщик для вычислений (fixed thread pool).
 */
public class ComputationScheduler implements Scheduler {
    private static final int N = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(N);

    @Override
    public void schedule(Runnable task) {
        EXEC.submit(task);
    }
}

