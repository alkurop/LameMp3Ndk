package com.omar.retromp3recorder.app.main;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.omar.retromp3recorder.app.App;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.customviews.VisualizerView;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;


public class MainActivityV2 extends AppCompatActivity implements MainView {

    private final PublishSubject<Action> actionPublishSubject = PublishSubject.create();
    private final PublishSubject<State> statePublishSubject = PublishSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ImageView playButton, recordButton, shareButton;
    private LinearLayout llLogHolder, radioContainer1, radioContainer2;
    private ScrollView scrollView;
    private TextView timerTV;
    private VisualizerView visualizerView;
    private ImageView backgroundView;
    private Visualizer mVisualizer;

    @Inject
    MainViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.appComponent.inject(this);
        findViews();
        setListeners();
        Disposable disposable = actionPublishSubject
                .compose(presenter.process())
                .observeOn(mainThread())
                .subscribe(this::renderView);
        compositeDisposable.add(disposable);
    }

    private void findViews() {
        playButton = findViewById(R.id.iv_play);
        recordButton = findViewById(R.id.iv_record);
        shareButton = findViewById(R.id.iv_share);
        llLogHolder = findViewById(R.id.ll_logHolder);
        scrollView = findViewById(R.id.scrollView);
        visualizerView = findViewById(R.id.visualizer);
        radioContainer1 = findViewById(R.id.ll_radio_container1);
        radioContainer2 = findViewById(R.id.ll_radio_container2);
        backgroundView = findViewById(R.id.background);
    }

    private void setListeners() {
        Disposable disposable = RxView.clicks(playButton)
                .map(unit -> statePublishSubject.blockingFirst())
                .subscribe(state -> {

                });

        Disposable disposable1 = RxView.clicks(recordButton)
                .map(unit -> statePublishSubject.blockingFirst())
                .subscribe(state -> {

                });

        Disposable disposable2 = RxView.clicks(shareButton)
                .map(unit -> statePublishSubject.blockingFirst())
                .subscribe(state -> {

                });

        compositeDisposable.addAll(disposable, disposable1, disposable2);
    }

    @Override
    public void renderView(MainViewModel mainViewModel) {
        renderPermissions(mainViewModel.requestForPermissions);
        renderBitrate(mainViewModel.bitRate);
        renderSampleRate(mainViewModel.sampleRate);
        renderError(mainViewModel.error);
        renderMessage(mainViewModel.message);
        renderState(mainViewModel.state);
        renderPlayerId(mainViewModel.playerId);
    }

    private void renderPlayerId(@Nullable Integer playerId) {

    }

    private void renderState(State state) {
        statePublishSubject.onNext(state);
        playButton.setImageResource(state == State.Playing ?
                R.drawable.ic_action_stop :
                R.drawable.ic_action_play);

        recordButton.setImageResource(state == State.Recording ?
                R.drawable.ic_action_rec :
                R.drawable.ic_action_stop);
    }

    private void renderMessage(@Nullable String message) {

    }

    private void renderError(@Nullable String error) {

    }

    private void renderSampleRate(VoiceRecorder.SampleRate sampleRate) {

    }

    private void renderBitrate(VoiceRecorder.BitRate bitRate) {

    }

    private void renderPermissions(@Nullable Set<String> requestForPermissions) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
