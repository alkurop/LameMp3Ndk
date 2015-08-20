package com.omar.lamemp3ndk.app.presenters;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import com.omar.lamemp3ndk.app.Constants;
import com.omar.lamemp3ndk.app.R;
import com.omar.lamemp3ndk.app.callbacks.IAudioControllerCallback;
import com.omar.lamemp3ndk.app.controllers.AudioStatesController;
import com.omar.lamemp3ndk.app.controllers.IAudioStatesEvents;
import com.omar.lamemp3ndk.app.controllers.ILsdDisplay;
import com.omar.lamemp3ndk.app.controllers.StateSelector;
import com.omar.lamemp3ndk.app.callbacks.ICheckboxCallback;
import com.omar.lamemp3ndk.app.share.SharingModule;
import com.omar.lamemp3ndk.app.utils.ContextHelper;
import com.omar.lamemp3ndk.app.views.IMainView;

/**
 * Created by omar on 17.08.15.
 */
public class MainPresenter implements IMainEvents, ILsdDisplay {

    private Context context;

    private IMainView view;
    private IAudioStatesEvents audioController;
    private IRadioGroupEvents bitRatePresenter;
    private IRadioGroupEvents sampleRatePresenter;

    private StateSelector stateSelector;

    public void Init(IMainView _view) {
        context = ContextHelper.GetContext();
        view = _view;
        setUpAudioController();
        setUpStatesSelector();
        DisplayView();
    }


    @Override
    public void RecordClicked() {
        stateSelector.RecordingClicked();
    }

    @Override
    public void PlayClicked() {
        stateSelector.PlayCLicked();
    }

    @Override
    public void ShareClicked() {
        stateSelector.StopAll();
        SharingModule.I().StartShading(audioController.GetFilePath(), this);

    }

    @Override
    public void StopAll() {
        stateSelector.StopAll();
    }

    private void DisplayView() {
        view.SetUI();
        setUpSampleRateRadioGroup(view.GetRadioContainer1());
        setUpBitRateRadioGroup(view.GetRadioContainer2());
    }

    private void setUpAudioController() {
        audioController = new AudioStatesController();
        audioController.Init(this, new IAudioControllerCallback() {
            @Override
            public void onPlayerStoped() {
                stateSelector.CallbackStop();
                view.StopVisualizer();
            }

            @Override
            public void onRecorderStoped() {
                view.StopVisualizer();
                stateSelector.CallbackStop();
            }

            @Override
            public void SetPlayerId(int id) {
                view.StartVisualizer(id);
            }

            @Override
            public void onPlayerError(String s) {
                MainPresenter.this.SetText(s);
                stateSelector.CallbackStop();
                view.StopVisualizer();
            }

            @Override
            public void onRecorderError(String s) {
                view.StopVisualizer();
                MainPresenter.this.SetText(s);
                stateSelector.CallbackStop();
            }
        });
    }

    private void setUpStatesSelector() {
        stateSelector = new StateSelector() {
            @Override
            public void StartRecording() {
                view.SetRecordBtnImg(R.drawable.ic_action_stop);
                audioController.StartRecord();
            }

            @Override
            public void StartPlaying() {
                view.SetPlayBtnImg(R.drawable.ic_action_stop);
                audioController.StartPlay();
            }

            @Override
            public void StopPlaying() {
                view.SetPlayBtnImg(R.drawable.ic_action_play);
                audioController.StopPlay();
                view.StopVisualizer();
            }

            @Override
            public void StopRecodring() {
                view.StopVisualizer();
                view.SetRecordBtnImg(R.drawable.ic_action_rec);
                audioController.StopRecord();

            }

            @Override
            public void OnCallbackStop() {
                view.StopVisualizer();
                view.SetRecordBtnImg(R.drawable.ic_action_rec);
                view.SetPlayBtnImg(R.drawable.ic_action_play);
            }
        };
    }

    private void setUpSampleRateRadioGroup(LinearLayout container) {
        String groupName = Constants.SAMPLE_RATE_LABEL;
        String format = Constants.HZ_LABEL;

        sampleRatePresenter = new RadioGroupPresenter();
        sampleRatePresenter.CreateRadioGroup(container, groupName, format, Constants.SAMPLE_RATE_PRESETS, new ICheckboxCallback() {
            @Override
            public void setDataIndex(int index) {
                audioController.SetReсorderHz(Constants.SAMPLE_RATE_PRESETS[index]);
            }
        });
        sampleRatePresenter.SetSelected(0);


    }

    private void setUpBitRateRadioGroup(LinearLayout container) {
        String groupName = context.getString(R.string.bit_rate);
        String format = context.getString(R.string.sample_rate);

        bitRatePresenter = new RadioGroupPresenter();
        bitRatePresenter.CreateRadioGroup(container, groupName, format, Constants.BIT_RATE_PRESETS, new ICheckboxCallback() {
            @Override
            public void setDataIndex(int index) {
                audioController.SetReсorderBPM(Constants.BIT_RATE_PRESETS[index]);
            }
        });
        bitRatePresenter.SetSelected(0);

    }

    @Override
    public void SetText(String text) {
        view.SetLabelText(text);
    }
}
