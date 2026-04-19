package com.hyzodiac.api.events;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * A minimal, thread-safe event dispatcher that collapses all registered listeners into
 * a single invoker of type {@code T}. Modeled after Fabric API's {@code Event} but
 * free of any platform dependency so it can live in the {@code common} module.
 *
 * @param <T> the listener interface type; typically a {@code @FunctionalInterface}
 */
public final class Event<T> {

    private final CopyOnWriteArrayList<T> listeners = new CopyOnWriteArrayList<>();
    private final Function<T[], T> invokerFactory;
    private final Class<? super T> type;
    private volatile T invoker;

    private Event(Class<? super T> type, Function<T[], T> invokerFactory) {
        this.type = type;
        this.invokerFactory = invokerFactory;
        rebuildInvoker();
    }

    /**
     * Creates a new event.
     *
     * @param type           the listener class (used for reflection-free array construction)
     * @param invokerFactory turns the current listener array into a single fan-out listener
     */
    public static <T> Event<T> create(Class<? super T> type, Function<T[], T> invokerFactory) {
        return new Event<>(Objects.requireNonNull(type), Objects.requireNonNull(invokerFactory));
    }

    public void register(T listener) {
        Objects.requireNonNull(listener, "listener");
        listeners.add(listener);
        rebuildInvoker();
    }

    public void unregister(T listener) {
        if (listeners.remove(listener)) {
            rebuildInvoker();
        }
    }

    public T invoker() {
        return invoker;
    }

    @SuppressWarnings("unchecked")
    private void rebuildInvoker() {
        T[] array = (T[]) java.lang.reflect.Array.newInstance(type, listeners.size());
        invoker = invokerFactory.apply(listeners.toArray(array));
    }
}
