package com.custom.rx.java.work.core;

/**
 * Функциональный интерфейс, описывающий логику эмиссии элементов.
 *
 * @param <T> тип элементов
 */
@FunctionalInterface
public interface OnSubscribe<T> {
    /**
     * Метод, вызывающий при подписке для передачи элементов наблюдателю.
     *
     * @param observer целевой наблюдатель
     */
    void subscribe(Observer<? super T> observer);
}

