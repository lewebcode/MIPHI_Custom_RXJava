// src/test/java/com/rxjavawork/schedulers/SchedulerTest.java
package com.custom.rx.java.work.schedulers;

import com.custom.rx.java.work.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    @Test
    void testIoSchedulerDoesNotBlock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadName = new AtomicReference<>();

        Observable.just("X")
                .subscribeOn(new IOScheduler())
                .subscribe(item -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(threadName.get().contains("pool"));
    }

    @Test
    void testSingleSchedulerSequential() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<String> first = new AtomicReference<>();
        AtomicReference<String> second = new AtomicReference<>();

        Observable.just("a")
                .observeOn(new SingleScheduler())
                .subscribe(item -> {
                    first.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        Observable.just("b")
                .observeOn(new SingleScheduler())
                .subscribe(item -> {
                    second.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertEquals(first.get(), second.get());
    }
}

