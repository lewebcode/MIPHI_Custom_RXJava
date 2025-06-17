package com.custom.rx.java.work.operators;

import com.custom.rx.java.work.core.CompositeDisposable;
import com.custom.rx.java.work.core.Disposable;
import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.core.Observer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Оператор flatMap: для каждого элемента исходного потока
 * создаёт новый Observable и «расплющивает» его элементы в единый результирующий поток.
 */
public class FlatMapOperator {

    /**
     * @param source исходный Observable
     * @param mapper функция, порождающая вложенный Observable для каждого элемента
     * @param <T>    тип исходных элементов
     * @param <R>    тип результирующих элементов
     * @return новый Observable<R>
     */
    public static <T, R> Observable<R> apply(
            Observable<T> source,
            Function<? super T, Observable<? extends R>> mapper
    ) {
        return Observable.create(observer -> {
            CompositeDisposable composite = new CompositeDisposable();
            AtomicInteger activeCount = new AtomicInteger(1); // 1 — родительский поток
            ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

            Disposable parentDisp = source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    activeCount.incrementAndGet();
                    Disposable innerDisp = mapper.apply(item)
                            .subscribe(new Observer<R>() {
                                @Override
                                public void onNext(R inner) {
                                    observer.onNext(inner);
                                }
                                @Override
                                public void onError(Throwable t) {
                                    errors.add(t);
                                    completeIfDone();
                                }
                                @Override
                                public void onComplete() {
                                    completeIfDone();
                                }
                            });
                    composite.add(innerDisp);
                }

                @Override
                public void onError(Throwable t) {
                    errors.add(t);
                    completeIfDone();
                }

                @Override
                public void onComplete() {
                    completeIfDone();
                }

                private void completeIfDone() {
                    if (activeCount.decrementAndGet() == 0) {
                        // если были ошибки — передаем первую
                        Throwable err = errors.poll();
                        if (err != null) {
                            observer.onError(err);
                        } else {
                            observer.onComplete();
                        }
                        composite.dispose();
                    }
                }
            });

            composite.add(parentDisp);
        });
    }
}
