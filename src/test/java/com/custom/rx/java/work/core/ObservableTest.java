package com.custom.rx.java.work.core;

import com.custom.rx.java.work.schedulers.IOScheduler;
import com.custom.rx.java.work.schedulers.SingleScheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {

    @Test
    void testCreateAndSubscribe() {
        List<String> received = new ArrayList<>();
        Observable<String> source = Observable.create(observer -> {
            observer.onNext("A");
            observer.onNext("B");
            observer.onComplete();
        });

        source.subscribe(received::add,
                Throwable::printStackTrace,
                () -> received.add("DONE"));

        assertEquals(List.of("A", "B", "DONE"), received);
    }

    @Test
    void testJustSingleElement() {
        List<Integer> list = new ArrayList<>();
        Observable.just(42).subscribe(list::add);

        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    void testSubscribeOnScheduler() throws InterruptedException {
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Observable.just("X")
                .subscribeOn(new IOScheduler())
                .subscribe(item -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotEquals(Thread.currentThread().getName(), threadName.get());
    }

    @Test
    void testObserveOnScheduler() throws InterruptedException {
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Observable.just("Y")
                .observeOn(new SingleScheduler())
                .subscribe(item -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(threadName.get().startsWith("pool-"));
    }
}

