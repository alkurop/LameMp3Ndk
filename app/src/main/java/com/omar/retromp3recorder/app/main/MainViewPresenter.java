package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.Interactor;
import com.omar.retromp3recorder.app.di.Presenter;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;

import static com.omar.retromp3recorder.app.main.MainView.BitRate;
import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.ErrorLogResult;
import static com.omar.retromp3recorder.app.main.MainView.MainViewAction;
import static com.omar.retromp3recorder.app.main.MainView.MainViewModel;
import static com.omar.retromp3recorder.app.main.MainView.MainViewResult;
import static com.omar.retromp3recorder.app.main.MainView.MessageLogResult;
import static com.omar.retromp3recorder.app.main.MainView.SampleRate;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeResult;

public class MainViewPresenter implements Presenter<MainViewAction, MainViewResult, MainViewModel> {

    private final Interactor<MainViewAction, MainViewResult> interactor;

    public MainViewPresenter(Interactor<MainViewAction, MainViewResult> interactor) {
        this.interactor = interactor;
    }

    @Override
    public ObservableTransformer<MainViewAction, MainViewModel> process() {
        return upstream -> upstream
                .compose(interactor.process())
                .scan(getDefaultViewModel(), getMapper());
    }

    private BiFunction<MainViewModel, MainViewResult, MainViewModel> getMapper() {
        return (oldState, result) -> {
            if (result instanceof MessageLogResult) {
                return new MainViewModel(
                        oldState.sampleRate,
                        oldState.bitRate,
                        ((MessageLogResult) result).message,
                        null
                );
            }
            if (result instanceof ErrorLogResult) {
                return new MainViewModel(
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        ((ErrorLogResult) result).error
                );
            }
            if (result instanceof BitrateChangedResult) {
                return new MainViewModel(
                        oldState.sampleRate,
                        ((BitrateChangedResult) result).bitRate,
                        null,
                        null
                );
            }
            if (result instanceof SampleRateChangeResult) {
                return new MainViewModel(
                        ((SampleRateChangeResult) result).sampleRate,
                        oldState.bitRate,
                        null,
                        null
                );
            }
            throw new IllegalStateException("Unable to map results");
        };

    }

    private MainViewModel getDefaultViewModel() {
        return new MainViewModel(
                SampleRate._44100,
                BitRate._320,
                null,
                null
        );
    }

}
