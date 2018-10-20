package com.omar.retromp3recorder.app.main;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.customviews.VisualizerView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;


public class MainActivityV2 extends AppCompatActivity implements MainView{

    private ImageView btn_Play, btn_Record, btn_Share;
    private LinearLayout llLogHolder, radioContainer1, radioContainer2;
    private ScrollView scrollView;
    private TextView tv_Timer;
    private VisualizerView mVisualizerView;
    private ImageView backgroundView;

    private MainViewPresenter presenter;

    private final PublishSubject <MainViewAction> actionPublishSubject = PublishSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Visualizer mVisualizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Play = findViewById(R.id.iv_play);
        btn_Record = findViewById(R.id.iv_record);
        btn_Share = findViewById(R.id.iv_share);
        llLogHolder = findViewById(R.id.ll_logHolder);
        scrollView = findViewById(R.id.scrollView);
        mVisualizerView = findViewById(R.id.visualizer);
        radioContainer1 = findViewById(R.id.ll_radio_container1);
        radioContainer2 = findViewById(R.id.ll_radio_container2);
        backgroundView = findViewById(R.id.background);
        btn_Play.setOnClickListener(v -> {

        });
        btn_Record.setOnClickListener(v -> {

        });
        btn_Share.setOnClickListener(v -> {

        });

        Disposable disposable = actionPublishSubject
                .compose(presenter.process())
                .observeOn(mainThread())
                .subscribe(this::renderView);
        compositeDisposable.add(disposable);
    }

    public void startVisualizer(int playerId) {
        mVisualizer = new Visualizer(playerId);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    public void stopVisualizer() {
        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }

    public void setLabelText(String s) {
        tv_Timer = (TextView) getLayoutInflater().inflate(R.layout.log_view, null);
        tv_Timer.setText(s);
        llLogHolder.addView(tv_Timer);
    }

    @Override
    public void renderView(MainViewModel mainViewModel) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
