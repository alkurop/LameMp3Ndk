package com.omar.retromp3recorder.app.mvi;

import io.reactivex.annotations.NonNull;

public class OneShot<T> {
    private volatile boolean isShot;
    private final T value;

    public OneShot(@NonNull T value) {
        this.value = value;
    }

    @NonNull
    public T getValueOnce() {
        isShot = true;
        return value;
    }

    @NonNull
    public T checkValue() {
        return value;
    }

    public boolean isShot() {
        return isShot;
    }
}
