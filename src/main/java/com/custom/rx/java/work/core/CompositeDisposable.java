package com.custom.rx.java.work.core;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CompositeDisposable для групповой отмены нескольких подписок.
 */
public class CompositeDisposable {
    private final Set<Disposable> disposables = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Добавляет Disposable в группу.
     *
     * @param d Disposable для добавления
     */
    public void add(Disposable d) {
        disposables.add(d);
    }

    /**
     * Удаляет Disposable из группы.
     *
     * @param d Disposable для удаления
     */
    public void remove(Disposable d) {
        disposables.remove(d);
    }

    /**
     * Отменяет все подписки в группе.
     */
    public void dispose() {
        for (Disposable d : disposables) {
            d.dispose();
        }
        disposables.clear();
    }

    /**
     * Проверяет, отменены ли все подписки.
     *
     * @return true, если все подписки отменены
     */
    public boolean isDisposed() {
        return disposables.stream().allMatch(Disposable::isDisposed);
    }
}

