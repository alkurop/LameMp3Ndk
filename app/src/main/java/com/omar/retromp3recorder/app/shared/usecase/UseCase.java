package com.omar.retromp3recorder.app.shared.usecase;

import io.reactivex.Completable;

public interface UseCase<T> {
    Completable execute(T params);
}
