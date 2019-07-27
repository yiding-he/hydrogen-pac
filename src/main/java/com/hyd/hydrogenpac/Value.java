package com.hyd.hydrogenpac;

public class Value<T> {

    public static <T> Value<T> empty() {
        return of(null);
    }

    public static <T> Value<T> of(T t) {
        return new Value<>(t);
    }

    private Value(T t) {
        this.value = t;
    }

    private T value;

    public T get() {
        return value;
    }

    public void set(T t) {
        this.value = t;
    }
}
