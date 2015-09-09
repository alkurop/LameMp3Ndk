package com.omar.retromp3recorder.app.views;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.customviews.VisualizerView;
import com.omar.retromp3recorder.app.presenters.IMainEvents;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import com.omar.retromp3recorder.app.utils.TouchController;


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
        presenter.init(this);
    }



    @Override
    public void onClick(View view) {
        if (TouchController.allowClick())
            switch (view.getId()) {
            case R.id.iv_play:
                presenter.playClicked();
                break;
            case R.id.iv_record:
                presenter.recordClicked();
                break;
            case R.id.iv_share:
                presenter.shareClicked();
                break;

        }

    }

    @Override
    public void setUI() {
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
    public void setRecordBtnImg(int drawable) {
        btn_Record.setImageResource(drawable);
    }

    @Override
    public void setPlayBtnImg(int drawable) {
        btn_Play.setImageResource(drawable);
    }

    @Override
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

    @Override
    public void stopVisualizer() {
        if (mVisualizer != null)  new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    Thread.sleep(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mVisualizer.release();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        } ).start();
    }

    @Override
    public void setLabelText(String s) {

        tv_Timer = (TextView) getLayoutInflater().inflate(R.layout.log_view, null);
        tv_Timer.setText(s);
        llLogHolder.addView(tv_Timer);
        new Thread(new Runnable() {
            @Override
            public void run(){
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
                }}

        }).start();

    }

    @Override
    public LinearLayout getRadioContainer1() {
        return llRadioContainer1;
    }

    @Override
    public LinearLayout getRadioContainer2() {
        return llRadioContainer2;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopAll();
    }

    /*FOR TESTING ONLY*/
    public MainPresenter getMainPresenter(){return new MainPresenter();}
    public void setMainPresenter(){presenter = getMainPresenter();}
}
