# Описание проекта

Проект представляет собой систему реактивных потоков, основанную на паттерне «Наблюдатель» (Observer), с поддержкой управления выполнением потоков и обработки событий. Включает базовые компоненты, операторы для преобразования данных, планировщики потоков и механизмы управления подпиской и её отменой.

## Основные функции

* **Observable** — источник данных, фабрики `create()`, `just()`.
* **Observer** — интерфейс с методами `onNext()`, `onError()`, `onComplete()`.
* **Операторы** (в пакете `com.custom.rx.java.work.operators`):

    * `MapOperator` (`map`)
    * `FilterOperator` (`filter`)
    * `FlatMapOperator` (`flatMap`)
    * `MergeOperator` (`merge`)
    * `ConcatOperator` (`concat`)
    * `ReduceOperator` (`reduce`)
* **Schedulers** (в пакете `com.custom.rx.java.work.schedulers`):

    * `IOScheduler` (cached thread pool)
    * `ComputationScheduler` (fixed thread pool)
    * `SingleScheduler` (single-thread executor)
* **Disposable**:

    * `Disposable` — отмена одной подписки
    * `CompositeDisposable` — групповая отмена
* **Логирование** через SLF4J + Log4j

## Технологии

* Java 17+
* Maven
* SLF4J API + Log4j
* JUnit 5

## Установка и запуск

1. Клонировать репозиторий:

   ```bash
   git clone https://github.com/ВАШ_ПРОЕКТ/javawork.git
   cd javawork
   ```
2. Собрать и запустить тесты:

   ```bash
   mvn clean test
   ```
3. Запустить демонстрацию:

   ```bash
   mvn exec:java -Dexec.mainClass="com.custom.rx.java.work.Main"
   ```

## Структура проекта

```plaintext
javawork/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── javawork/
│   │   │           ├── core/
│   │   │           │   ├── Observable.java
│   │   │           │   ├── Observer.java
│   │   │           │   ├── OnSubscribe.java
│   │   │           │   ├── Disposable.java
│   │   │           │   └── CompositeDisposable.java
│   │   │           ├── operators/
│   │   │           │   ├── MapOperator.java
│   │   │           │   ├── FilterOperator.java
│   │   │           │   ├── FlatMapOperator.java
│   │   │           │   ├── MergeOperator.java
│   │   │           │   ├── ConcatOperator.java
│   │   │           │   └── ReduceOperator.java
│   │   │           ├── schedulers/
│   │   │           │   ├── Scheduler.java
│   │   │           │   ├── IOScheduler.java
│   │   │           │   ├── ComputationScheduler.java
│   │   │           │   └── SingleScheduler.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── log4j.properties
│   └── test/
│       └── java/
│           └── com/
│               └── javawork/
│                   ├── core/
│                   │   └── ObservableTest.java
│                   ├── operators/
│                   │   └── OperatorTest.java
│                   └── schedulers/
│                       └── SchedulerTest.java
```

## Архитектура системы

1. **Паттерн Observer**:

    * Источник (`Observable`) делегирует эмиссию элементов через `OnSubscribe`.
    * Потребитель реализует `Observer` или передаёт лямбды в `subscribe()`.
    * `Disposable` контролирует отмену, `CompositeDisposable` — групповую отмену.

2. **Структура пакетов**:

    * `core` — базовые компоненты и фабрики.
    * `operators` — классы-операторы для модульности.
    * `schedulers` — управление планировщиками потоков.

3. **Flow**:

    * Построение цепочки: `Observable.create(...)` → операторы → `subscribeOn()`/`observeOn()` → `subscribe()`.
    * Все переходы потоков выполняются через `Scheduler.schedule(...)`.

## Принципы работы Schedulers

| Scheduler                  | Реализация               | Применение                 |
| -------------------------- | ------------------------ | -------------------------- |
| **IOScheduler**          | `CachedThreadPool`       | I/O задачи, сеть           |
| **ComputationScheduler** | `FixedThreadPool(N=CPU)` | CPU-bound вычисления       |
| **SingleScheduler**      | `SingleThreadExecutor`   | Последовательная обработка |

* `subscribeOn()` определяет поток подписки.
* `observeOn()` переключает поток обработки событий.

## Тестирование

В проекте написаны юнит-тесты JUnit 5 для ключевых сценариев:

1. **Базовая работа**

    * `create()` + `subscribe(onNext, onError, onComplete)`
    * `just()`, проверка эмиссии и завершения.
2. **Операторы**

    * `map`, `filter`
    * `flatMap`, `merge`, `concat`, `reduce`
3. **Планировщики**

    * `subscribeOn`/`observeOn` проверяют переключение потоков.
4. **Обработка ошибок**

    * Эмит `onError`, проверка прекращения `onNext`.
5. **Отмена подписки**

    * `Disposable.dispose()`, `CompositeDisposable.dispose()`.

## Примеры использования

```bash
// map + filter + планировщики
MapOperator.apply(
    Observable.just(1,2,3,4,5),
    i -> i * 2
)
.subscribeOn(new IOScheduler())
.observeOn(new SingleScheduler())
.subscribe(
    i -> System.out.println("-> " + i),
    Throwable::printStackTrace,
    () -> System.out.println("Done")
);

// flatMap
FlatMapOperator.apply(
    Observable.just("A","B"),
    s -> Observable.just(s + "1", s + "2")
).subscribe(System.out::println);
```
