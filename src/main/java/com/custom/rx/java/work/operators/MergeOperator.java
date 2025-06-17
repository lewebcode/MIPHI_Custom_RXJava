package com.custom.rx.java.work.operators;

import com.custom.rx.java.work.core.CompositeDisposable;
import com.custom.rx.java.work.core.Disposable;
import com.custom.rx.java.work.core.Observable;
import com.custom.rx.java.work.core.Observer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Оператор merge: параллельно сливает несколько Observable в один поток.
 */
public class MergeOperator {

    /**
     * @param sources массив Observable-источников
     * @param <T>     тип элементов
     * @return новый Observable<T>, эмитирующий все элементы sources
     */
    @SafeVarargs
    public static <T> Observable<T> apply(Observable<? extends T>... sources) {
        return Observable.create(observer -> {
            CompositeDisposable composite = new CompositeDisposable();
            AtomicInteger remaining = new AtomicInteger(sources.length);
            ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

            for (Observable<? extends T> src : sources) {
                Disposable disp = src.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        observer.onNext(item);
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
                        if (remaining.decrementAndGet() == 0) {
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
                composite.add(disp);
            }
        });
    }
}
