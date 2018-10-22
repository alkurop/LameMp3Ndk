package com.omar.retromp3recorder.app.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails;
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails;
import com.github.alkurop.jpermissionmanager.PermissionsManager;
import com.jakewharton.rxbinding3.view.RxView;
import com.omar.retromp3recorder.app.App;
import com.omar.retromp3recorder.app.Constants;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.customviews.VisualizerView;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashMap;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.computation;


public class MainActivityV2 extends AppCompatActivity implements MainView {

    private final PublishSubject<Action> actionPublishSubject = PublishSubject.create();
    private final Subject<State> stateSubject = BehaviorSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Handler scrollDownHandler = new Handler();

    private ImageView playButton, recordButton, shareButton;
    private LinearLayout logContainerView, sampleRateContainer, bitRateContainer;
    private List<RadioButton> sampleRateGroup;
    private List<RadioButton> bitRateGroup;
    private ScrollView scrollView;
    private VisualizerView visualizerView;

    @Nullable
    private Visualizer visualizer;
    private PermissionsManager permissionsManager;
    private HashMap<String, PermissionOptionalDetails> permissionsMap;
    private LayoutInflater layoutInflater;

    @Inject
    MainViewInteractor interactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = LayoutInflater.from(this);
        App.appComponent.inject(this);
        permissionsManager = new PermissionsManager(this);
        findViews();
        setListeners();
        createPermissionsMap();
        Disposable disposable = actionPublishSubject
                .compose(interactor.process())
                .compose(MainViewResultMapper.map())
                .observeOn(mainThread())
                .subscribe(this::renderView);
        compositeDisposable.add(disposable);
    }

    private void createPermissionsMap() {
        permissionsMap = createHashMap(
                new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        new PermissionRequiredDetails(
                                getString(R.string.write_permission_title),
                                getString(R.string.write_permission_message),
                                getString(R.string.write_required_message))
                ),
                new Pair<>(Manifest.permission.RECORD_AUDIO,
                        new PermissionRequiredDetails(
                                getString(R.string.write_permission_title),
                                getString(R.string.write_permission_message),
                                getString(R.string.write_required_message)
                        )
                )
        );
    }

    private void findViews() {
        playButton = findViewById(R.id.iv_play);
        recordButton = findViewById(R.id.iv_record);
        shareButton = findViewById(R.id.iv_share);
        logContainerView = findViewById(R.id.ll_logHolder);
        scrollView = findViewById(R.id.scrollView);
        visualizerView = findViewById(R.id.visualizer);
        sampleRateContainer = findViewById(R.id.ll_radio_container1);
        bitRateContainer = findViewById(R.id.ll_radio_container2);
        prefillRadioButtons();
    }

    @SuppressLint("DefaultLocale")
    private void prefillRadioButtons() {
        addTitleView(bitRateContainer, getString(R.string.bit_rate));
        addTitleView(sampleRateContainer, getString(R.string.sample_rate));

        sampleRateGroup = new ArrayList<>();
        bitRateGroup = new ArrayList<>();

        for (VoiceRecorder.SampleRate sampleRate : VoiceRecorder.SampleRate.values()) {
            String title = String.format("%d %s", sampleRate.value, Constants.KB);
            RadioButton radioButton = addCheckBox(sampleRateContainer, title);
            sampleRateGroup.add(radioButton);
        }

        for (VoiceRecorder.BitRate bitRate : VoiceRecorder.BitRate.values()) {
            String title = String.format("%d %s", bitRate.value, Constants.KBPS);
            RadioButton radioButton = addCheckBox(bitRateContainer, title);
            bitRateGroup.add(radioButton);
        }
    }

    private RadioButton addCheckBox(ViewGroup container, String text) {
        @SuppressLint("InflateParams")
        RadioButton cb  = (RadioButton) layoutInflater.inflate(R.layout.checkbox, null);
        cb.setText(text);
        cb.setHeight(this.getResources().getDimensionPixelSize(R.dimen.cb_height));
        container.addView(cb);
        return cb;
    }

    private void addTitleView(ViewGroup container, String title) {
        layoutInflater.inflate(R.layout.container_title, container);
        TextView titleView = container.findViewById(R.id.title);
        titleView.setText(title);
    }

    private void setListeners() {
        Disposable disposable = RxView.clicks(playButton)
                .map(unit -> stateSubject.blockingFirst())
                .subscribe(state -> {
                    switch (state) {
                        case Idle:
                        case Recording: {
                            actionPublishSubject.onNext(new PlayAction());
                            break;
                        }
                        case Playing: {
                            actionPublishSubject.onNext(new StopAction());
                            break;
                        }
                    }
                });

        Disposable disposable1 = RxView.clicks(recordButton)
                .map(unit -> stateSubject.blockingFirst())
                .subscribe(state -> {
                    switch (state) {
                        case Idle:
                        case Playing: {
                            actionPublishSubject.onNext(new RecordAction());
                            break;
                        }
                        case Recording: {
                            actionPublishSubject.onNext(new StopAction());
                            break;
                        }
                    }
                });

        Disposable disposable2 = RxView.clicks(shareButton)
                .subscribe(unit ->
                        actionPublishSubject.onNext(new ShareAction())
                );

        compositeDisposable.addAll(disposable, disposable1, disposable2);

        for (int i = 0; i < bitRateGroup.size(); i++) {
            final int index = i;
            RadioButton radioButton = bitRateGroup.get(index);
            Disposable disposableRadio = RxView.clicks(radioButton)
                    .subscribe(unit -> {
                        VoiceRecorder.BitRate bitRate = VoiceRecorder
                                .BitRate.values()[index];
                        actionPublishSubject.onNext(new BitRateChangeAction(bitRate));
                    });
            compositeDisposable.add(disposableRadio);
        }

        for (int i = 0; i < bitRateGroup.size(); i++) {
            final int index = i;
            RadioButton radioButton = bitRateGroup.get(index);
            Disposable disposableRadio = RxView.clicks(radioButton)
                    .subscribe(unit -> {
                        VoiceRecorder.SampleRate sampleRate = VoiceRecorder
                                .SampleRate.values()[index];
                        actionPublishSubject.onNext(new SampleRateChangeAction(sampleRate));

                    });
            compositeDisposable.add(disposableRadio);
        }
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
        if (playerId == null) {
            return;
        }
        visualizer = new Visualizer(playerId);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                visualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }

    private void stopVisualizer() {
        if (visualizer != null) {
            Disposable disposable = Completable
                    .fromAction(() -> {
                        visualizer.release();
                        visualizer = null;
                    })
                    .subscribeOn(computation())
                    .subscribe();
            compositeDisposable.add(disposable);
        }
    }

    private void renderState(State state) {
        stateSubject.onNext(state);
        playButton.setImageResource(state == State.Playing ?
                R.drawable.ic_action_stop :
                R.drawable.ic_action_play);

        recordButton.setImageResource(state == State.Recording ?
                R.drawable.ic_action_rec :
                R.drawable.ic_action_stop);
        if (state == State.Idle) stopVisualizer();
    }

    private void renderMessage(@Nullable String message) {
        if (message == null) {
            return;
        }
        TextView inflate = (TextView) getLayoutInflater().inflate(R.layout.log_view, logContainerView);
        inflate.setText(message);
        scrollDownHandler.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 150);
    }

    private void renderError(@Nullable String error) {
        renderMessage(error);
    }

    private void renderPermissions(@Nullable Set<String> requestForPermissions) {
        if (requestForPermissions == null) {
            return;
        }
        HashMap<String, PermissionOptionalDetails> permissionRequests = new HashMap<>();
        for (String permissionName : requestForPermissions) {
            permissionRequests.put(permissionName, permissionsMap.get(permissionName));
        }
        permissionsManager.addPermissions(permissionRequests);
        permissionsManager.makePermissionRequest(true);
    }

    private void renderSampleRate(VoiceRecorder.SampleRate sampleRate) {
        for (int i = 0; i < sampleRateGroup.size(); i++) {
            sampleRateGroup.get(i).setChecked(sampleRate.ordinal() == i);
        }
    }

    private void renderBitrate(VoiceRecorder.BitRate bitRate) {
        for (int i = 0; i < bitRateGroup.size(); i++) {
            bitRateGroup.get(i).setChecked(bitRate.ordinal() == i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        scrollDownHandler.removeCallbacksAndMessages(null);
        stopVisualizer();
    }
}
