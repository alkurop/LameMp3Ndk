package com.omar.retromp3recorder.app.mvi;


import io.reactivex.ObservableTransformer;

public interface ResultMapper<Result, ViewModel> {

    ObservableTransformer<Result, ViewModel> map();

}
