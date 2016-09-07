package com.omar.retromp3recorder.app.presenters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

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
    private State state;

    public void init(IMainView _view) {
        context = ContextHelper.getContext();
        view = _view;
        setUpAudioController();
        setUpStatesSelector();
        displayView();
    }
    @Override
    public void setText(String text) {
        view.setLabelText(text);
    }

    @Override
    public void recordClicked() {
        stateSelector.recordingClicked();
    }

    @Override
    public void playClicked() {
        stateSelector.playCLicked();
    }

    @Override
    public void shareClicked() {
        stateSelector.stopAll();
        getSharingModule().startShading(audioController.getFilePath(), this);

    }

    @Override
    public void stopAll() {
        stateSelector.stopAll();
    }

    public void displayView() {
        view.setUI();
        setUpSampleRateRadioGroup(view.getRadioContainer1());
        setUpBitRateRadioGroup(view.getRadioContainer2());
    }

    @Override
    public Parcelable saveState () {
        return new State(bitRatePresenter.saveState(), sampleRatePresenter.saveState());
    }

    @Override
    public void restoreState (Parcelable parcelable) {
      state = ((State) parcelable);
    }

    @Override
    public void hardfixRestoreState () {
        if (state != null) {
            sampleRatePresenter.restoreState(state.sampleRateState);
            bitRatePresenter.restoreState(state.bitRateState);
        }
    }

    public void setUpAudioController() {
        audioController = new AudioStatesController();
        audioController.init(this, new IAudioControllerCallback() {
            @Override
            public void onPlayerStoped() {
                view.stopVisualizer();
                stateSelector.callbackStop();

            }

            @Override
            public void onRecorderStoped() {

                stateSelector.callbackStop();
            }

            @Override
            public void SetPlayerId(int id) {
                view.startVisualizer(id);
            }

            @Override
            public void onPlayerError(String s) {
                MainPresenter.this.setText(s);

                view.stopVisualizer();
                stateSelector.callbackStop();
            }

            @Override
            public void onRecorderError(String s) {
                MainPresenter.this.setText(s);
                stateSelector.callbackStop();
            }
        });
    }

    public void setUpStatesSelector() {
        stateSelector = new StateSelector() {
            @Override
            public void startRecording() {
                view.setRecordBtnImg(R.drawable.ic_action_stop);
                audioController.startRecord();
            }

            @Override
            public void startPlaying() {
                view.setPlayBtnImg(R.drawable.ic_action_stop);
                audioController.startPlay();
            }

            @Override
            public void stopPlaying() {
                view.setPlayBtnImg(R.drawable.ic_action_play);

                view.stopVisualizer();
                audioController.stopPlay();
            }

            @Override
            public void stopRecodring() {
                view.setRecordBtnImg(R.drawable.ic_action_rec);
                audioController.stopRecord();

            }

            @Override
            public void onCallbackStop() {
                view.stopVisualizer();
                view.setRecordBtnImg(R.drawable.ic_action_rec);
                view.setPlayBtnImg(R.drawable.ic_action_play);
            }
        };
    }

    public void setUpSampleRateRadioGroup(LinearLayout container) {
        String groupName = Constants.SAMPLE_RATE_LABEL;
        String format = Constants.HZ_LABEL;

        sampleRatePresenter = new RadioGroupPresenter();
        sampleRatePresenter.createRadioGroup(container, groupName, format, Constants.SAMPLE_RATE_PRESETS, new ICheckboxCallback() {
            @Override
            public void setDataIndex(int index) {
                audioController.setReсorderHz(Constants.SAMPLE_RATE_PRESETS[index]);
            }
        });
        sampleRatePresenter.setSelected(0);
    }

    public void setUpBitRateRadioGroup(LinearLayout container) {
        String groupName = context.getString(R.string.bit_rate);
        String format = context.getString(R.string.sample_rate);

        bitRatePresenter = new RadioGroupPresenter();
        bitRatePresenter.createRadioGroup(container, groupName, format, Constants.BIT_RATE_PRESETS, new ICheckboxCallback() {
            @Override
            public void setDataIndex(int index) {
                audioController.setReсorderBPM(Constants.BIT_RATE_PRESETS[index]);
            }
        });
        bitRatePresenter.setSelected(0);

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

    static class State implements Parcelable {
        RadioGroupPresenter.RadioGroupState bitRateState;
        RadioGroupPresenter.RadioGroupState sampleRateState;

        public State (RadioGroupPresenter.RadioGroupState bitRateState, RadioGroupPresenter.RadioGroupState sampleRateState) {
            this.bitRateState = bitRateState;
            this.sampleRateState = sampleRateState;
        }

        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel dest, int flags) {
            dest.writeParcelable(this.bitRateState, flags);
            dest.writeParcelable(this.sampleRateState, flags);
        }

        protected State (Parcel in) {
            this.bitRateState = in.readParcelable(RadioGroupPresenter.RadioGroupState.class.getClassLoader());
            this.sampleRateState = in.readParcelable(RadioGroupPresenter.RadioGroupState.class.getClassLoader());
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel (Parcel source) {
                return new State(source);
            }

            @Override
            public State[] newArray (int size) {
                return new State[size];
            }
        };
    }
}
