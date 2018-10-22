package com.omar.retromp3recorder.app.mvi;


import io.reactivex.ObservableTransformer;

public interface Presenter<Action, ViewModel> {

    ObservableTransformer<Action, ViewModel> process();

}
