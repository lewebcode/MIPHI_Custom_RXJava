package com.custom.rx.java.work.operators;

import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.core.Observer;

/**
 * Оператор concat: последовательная конкатенация двух Observable.
 */
public class ConcatOperator {
    public static <T> Observable<T> apply(
            Observable<? extends T> first,
            Observable<? extends T> second
    ) {
        return Observable.create(observer -> {
            first.subscribe(new Observer<T>() {
                @Override public void onNext(T item) { observer.onNext(item); }
                @Override public void onError(Throwable t) { observer.onError(t); }
                @Override public void onComplete() {
                    second.subscribe(observer);
                }
            });
        });
    }
}

