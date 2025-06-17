package com.custom.rx.java.work.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Планировщик единственного потока.
 */
public class SingleScheduler implements Scheduler {
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    @Override
    public void schedule(Runnable task) {
        EXEC.submit(task);
    }
}

