package com.omar.lamemp3ndk.app.views;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.omar.lamemp3ndk.app.R;
import com.omar.lamemp3ndk.app.customviews.VisualizerView;
import com.omar.lamemp3ndk.app.presenters.IMainEvents;
import com.omar.lamemp3ndk.app.presenters.MainPresenter;
import com.omar.lamemp3ndk.app.utils.TouchController;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {

    private ImageView btn_Play, btn_Record, btn_Share;
    private LinearLayout llLogHolder, llRadioContainer1, llRadioContainer2;
    private IMainEvents presenter;
    private ScrollView scrollView;
    private TextView tv_Timer;
    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.Init(this);
    }


    @Override
    public void onClick(View view) {
        if (TouchController.allowClick())
            switch (view.getId()) {
            case R.id.iv_play:
                presenter.PlayClicked();
                break;
            case R.id.iv_record:
                presenter.RecordClicked();
                break;
            case R.id.iv_share:
                presenter.ShareClicked();
                break;

        }

    }

    @Override
    public void SetUI() {
        setContentView(R.layout.activity_main);
        btn_Play = (ImageView) findViewById(R.id.iv_play);
        btn_Record = (ImageView) findViewById(R.id.iv_record);
        btn_Share = (ImageView) findViewById(R.id.iv_share);
        llLogHolder = (LinearLayout) findViewById(R.id.ll_logHolder);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizer);
        llRadioContainer1 = (LinearLayout) findViewById(R.id.ll_radio_container1);
        llRadioContainer2 = (LinearLayout) findViewById(R.id.ll_radio_container2);


        btn_Play.setOnClickListener(this);
        btn_Record.setOnClickListener(this);
        btn_Share.setOnClickListener(this);
    }

    @Override
    public void SetRecordBtnImg(int drawable) {
        btn_Record.setImageResource(drawable);
    }

    @Override
    public void SetPlayBtnImg(int drawable) {
        btn_Play.setImageResource(drawable);
    }

    @Override
    public void StartVisualizer(int playerId) {
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

    @Override
    public void StopVisualizer() {
        if (mVisualizer != null) mVisualizer.release();
    }

    @Override
    public void SetLabelText(String s) {

        tv_Timer = (TextView) getLayoutInflater().inflate(R.layout.log_view, null);
        tv_Timer.setText(s);
        llLogHolder.addView(tv_Timer);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public LinearLayout GetRadioContainer1() {
        return llRadioContainer1;
    }

    @Override
    public LinearLayout GetRadioContainer2() {
        return llRadioContainer2;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.StopAll();
    }
}
