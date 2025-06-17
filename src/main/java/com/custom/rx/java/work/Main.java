package com.custom.rx.java.work;

import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.operators.*;
import com.custom.rx.java.work.schedulers.ComputationScheduler;
import com.custom.rx.java.work.schedulers.IOScheduler;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Пример 1: Демонстрация работы с map + filter + subscribeOn/observeOn
        Observable<Integer> source = Observable.just(1, 2, 3, 4, 5);
        FilterOperator.apply(
                        MapOperator.apply(source, i -> i * 20),
                        i -> i >= 40
                )
                .subscribeOn(new IOScheduler())
                .observeOn(new ComputationScheduler())
                .subscribe(
                        i -> System.out.println("Received: " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Completed\n")
                );

        // Дадим асинхронным задачам завершиться
        Thread.sleep(500);

        // Пример 2: Демонстрация работы с flatMap
        FlatMapOperator.apply(source, i ->
                Observable.just(i, i * i)
        ).subscribe(i -> System.out.println("flatMap: " + i));

        // Пример 3: Демонстрация работы с merge
        MergeOperator.apply(
                Observable.just("A", "B"),
                Observable.just("3", "4")
        ).subscribe(s -> System.out.println("merge: " + s));

        // Пример 4: Демонстрация работы с concat
        ConcatOperator.apply(
                Observable.just("C", "D"),
                Observable.just("E")
        ).subscribe(s -> System.out.println("concat: " + s));
    }
}
