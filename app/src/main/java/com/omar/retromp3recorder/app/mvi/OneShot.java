package com.omar.retromp3recorder.app.mvi;

public class OneShot<T> {
    private volatile boolean isShot;
    private final T value;

    public OneShot(T value) {
        this.value = value;
    }

    public T getValue() {
        isShot = true;
        return value;
    }

    public boolean isShot() {
        return isShot;
    }
}
