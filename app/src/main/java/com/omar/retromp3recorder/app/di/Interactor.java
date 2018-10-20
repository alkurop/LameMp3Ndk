package com.omar.retromp3recorder.app.di;

import io.reactivex.ObservableTransformer;

public interface Interactor <Action, Result>{

    ObservableTransformer<Action, Result> process();

}
