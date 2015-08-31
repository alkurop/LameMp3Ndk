package com.omar.retromp3recorder.app.presenters;

import android.content.Context;
import android.widget.LinearLayout;
import com.omar.retromp3recorder.app.Constants;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.callbacks.IAudioControllerCallback;
import com.omar.retromp3recorder.app.callbacks.ICheckboxCallback;
import com.omar.retromp3recorder.app.controllers.AudioStatesController;
import com.omar.retromp3recorder.app.controllers.IAudioStatesEvents;
import com.omar.retromp3recorder.app.controllers.ILsdDisplay;
import com.omar.retromp3recorder.app.controllers.StateSelector;
import com.omar.retromp3recorder.app.share.IShadingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.utils.ContextHelper;
import com.omar.retromp3recorder.app.views.IMainView;

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
    public void SetText(String text) {
        view.SetLabelText(text);
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
        getSharingModule().StartShading(audioController.GetFilePath(), this);

    }

    @Override
    public void StopAll() {
        stateSelector.StopAll();
    }

    public void DisplayView() {
        view.SetUI();
        setUpSampleRateRadioGroup(view.GetRadioContainer1());
        setUpBitRateRadioGroup(view.GetRadioContainer2());
    }

    public void setUpAudioController() {
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

    public void setUpStatesSelector() {
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

    public void setUpSampleRateRadioGroup(LinearLayout container) {
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

    public void setUpBitRateRadioGroup(LinearLayout container) {
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

    public IShadingModule getSharingModule(){
        return SharingModule.I();
    }



    /*FOR UNIT TEST ONLY
    * DO NOT USE THESE METHODS*/

    public void setStateSelector (StateSelector stateSelector)
    {this.stateSelector = stateSelector;}

    public void setAudioController (AudioStatesController audioController){
        this.audioController = audioController;
    }


    public void setView(IMainView view){this.view = view;}


    public Context getContext() {
        return context;
    }

    public IMainView getView() {
        return view;
    }

    public IAudioStatesEvents getAudioController() {
        return audioController;
    }

    public IRadioGroupEvents getBitRatePresenter() {
        return bitRatePresenter;
    }

    public IRadioGroupEvents getSampleRatePresenter() {
        return sampleRatePresenter;
    }

    public StateSelector getStateSelector() {
        return stateSelector;
    }
}
