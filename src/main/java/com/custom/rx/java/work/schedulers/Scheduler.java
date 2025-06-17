package com.custom.rx.java.work.schedulers;

/**
 * Интерфейс планировщика задач.
 */
public interface Scheduler {
    /**
     * Запланировать выполнение задачи.
     *
     * @param task Runnable-задание
     */
    void schedule(Runnable task);
}

