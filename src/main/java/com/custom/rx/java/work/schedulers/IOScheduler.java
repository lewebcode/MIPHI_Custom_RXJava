package com.custom.rx.java.work.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Планировщик для I/O-потоков (cached thread pool).
 */
public class IOScheduler implements Scheduler {
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();

    @Override
    public void schedule(Runnable task) {
        EXEC.submit(task);
    }
}

