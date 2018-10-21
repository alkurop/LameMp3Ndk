package com.omar.retromp3recorder.app.mvi;

import io.reactivex.ObservableTransformer;

public interface Interactor <Action, Result>{

    ObservableTransformer<Action, Result> process();

}
