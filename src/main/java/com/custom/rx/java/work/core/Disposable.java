package com.custom.rx.java.work.core;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Реализация механизма отмены подписки.
 */
public class Disposable {
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    /**
     * Отменяет подписку и прекращает доставку событий.
     */
    public void dispose() {
        disposed.set(true);
    }

    /**
     * Проверяет, отменена ли подписка.
     *
     * @return true, если уже отменено
     */
    public boolean isDisposed() {
        return disposed.get();
    }
}

