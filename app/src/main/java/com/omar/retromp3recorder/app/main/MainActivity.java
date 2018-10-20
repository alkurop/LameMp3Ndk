package com.omar.retromp3recorder.app.main;

import android.Manifest;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails;
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails;
import com.github.alkurop.jpermissionmanager.PermissionsManager;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.customviews.VisualizerView;
import com.omar.retromp3recorder.app.presenters.IMainEvents;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import com.omar.retromp3recorder.app.utils.TouchController;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {

    private ImageView btn_Play, btn_Record, btn_Share;
    private LinearLayout llLogHolder, radioContainer1, radioContainer2;
    private IMainEvents presenter;
    private ScrollView scrollView;
    private TextView tv_Timer;
    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;
    private PermissionsManager permissionManager;
    private ImageView background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.init(this);
        permissionManager = new PermissionsManager(this);
        HashMap<String, PermissionOptionalDetails> permissions = new HashMap<>();
        permissions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionRequiredDetails(getString(R.string.write_permission_title),
                        getString(R.string.write_permission_message),
                        getString(R.string.write_required_message)));
        permissions.put(Manifest.permission.RECORD_AUDIO,
                new PermissionRequiredDetails(getString(R.string.write_permission_title),
                        getString(R.string.write_permission_message),
                        getString(R.string.write_required_message)));
        permissionManager.addPermissions(permissions);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle b = outState == null ? new Bundle() : outState;
        b.putParcelable("presenter", presenter.saveState());
        super.onSaveInstanceState(b);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        presenter.restoreState(savedInstanceState.getParcelable("presenter"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionManager.addPermissionsListener(new PermissionsManager.PermissionListener() {
            @Override
            public void onPermissionResult(HashMap<String, Boolean> stringBooleanMap) {
                permissionManager.clearPermissionsListeners();
                for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
                    if (!entry.getValue()) {
                        return;
                    }
                }
            }
        });
        permissionManager.makePermissionRequest(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hardFixRestorePresenter();
    }

    private void hardFixRestorePresenter() {
        presenter.hardfixRestoreState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionManager.onActivityResult(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
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
        btn_Play = findViewById(R.id.iv_play);
        btn_Record = findViewById(R.id.iv_record);
        btn_Share = findViewById(R.id.iv_share);
        llLogHolder = findViewById(R.id.ll_logHolder);
        scrollView = findViewById(R.id.scrollView);
        mVisualizerView = findViewById(R.id.visualizer);
        radioContainer1 = findViewById(R.id.ll_radio_container1);
        radioContainer2 = findViewById(R.id.ll_radio_container2);
        background = findViewById(R.id.background);
        Picasso.with(this).load(R.drawable.bg).fit().into(background);

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
        if (mVisualizer != null) new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    @Override
    public void setLabelText(String s) {

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
    public LinearLayout getRadioContainer1() {
        return radioContainer1;
    }

    @Override
    public LinearLayout getRadioContainer2() {
        return radioContainer2;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopAll();
    }

    /*FOR TESTING ONLY*/
    public MainPresenter getMainPresenter() {
        return new MainPresenter();
    }

    public void setMainPresenter() {
        presenter = getMainPresenter();
    }
}
