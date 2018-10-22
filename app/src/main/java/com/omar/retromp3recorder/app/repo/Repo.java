package com.omar.retromp3recorder.app.repo;

import io.reactivex.Observable;

public interface Repo<T> {

    Observable<T> observe();

    void newValue(T newValue);

}
