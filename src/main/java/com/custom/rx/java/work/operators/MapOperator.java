package com.custom.rx.java.work.operators;

import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.core.Observer;
import com.custom.rx.java.work.core.Disposable;

import java.util.function.Function;

/**
 * Оператор map: применяет функцию к каждому элементу потока.
 */
public class MapOperator {
    public static <T, R> Observable<R> apply(
            Observable<T> source,
            Function<? super T, ? extends R> mapper
    ) {
        return Observable.create(observer -> {
            Disposable disp = source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    observer.onNext(mapper.apply(item));
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

