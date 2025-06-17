package com.custom.rx.java.work.operators;

import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.core.Observer;
import com.custom.rx.java.work.core.Disposable;

import java.util.function.Predicate;

/**
 * Оператор filter: пропускает только те элементы, которые удовлетворяют предикату.
 */
public class FilterOperator {
    public static <T> Observable<T> apply(
            Observable<T> source,
            Predicate<? super T> predicate
    ) {
        return Observable.create(observer -> {
            Disposable disp = source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    if (predicate.test(item)) {
                        observer.onNext(item);
                    }
                }
                @Override
                public void onError(Throwable t) {
                    observer.onError(t);
                }
                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            });
        });
    }
}

