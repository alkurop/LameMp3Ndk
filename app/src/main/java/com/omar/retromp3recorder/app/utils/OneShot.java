package com.omar.retromp3recorder.app.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.annotations.NonNull;

public class OneShot<T> {
    private final AtomicBoolean isShot = new AtomicBoolean();
    private final T value;

    public OneShot(@NonNull T value) {
        this.value = value;
        isShot.set(false);
    }

    @NonNull
    public T getValueOnce() {
        isShot.set(true);
        return value;
    }

    @NonNull
    public T checkValue() {
        return value;
    }

    public boolean isShot() {
        return isShot.get();
    }
}
