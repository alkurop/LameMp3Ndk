package com.omar.retromp3recorder.app.di;


import io.reactivex.ObservableTransformer;

public interface Presenter<Action, Result, ViewModel> {

    ObservableTransformer<Action, ViewModel> process();

}
