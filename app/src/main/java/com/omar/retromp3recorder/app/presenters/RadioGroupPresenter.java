package com.omar.retromp3recorder.app.presenters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.callbacks.ICheckboxCallback;

/**
 * Created by omar on 19.08.15.
 */
public class RadioGroupPresenter implements IRadioGroupEvents, View.OnClickListener {
    private ICheckboxCallback callback;

    private LayoutInflater li;
    private Context context;

    private LinearLayout container;

    private String format;
    private String[] checkBoxNames;


    @Override
    public void createRadioGroup(final LinearLayout _container, final String _containerName, final String _format, final int[] _intData, final ICheckboxCallback _callback) {
        container = _container;
        context = _container.getContext();
        callback = _callback;

        if (_format == null) format = "";
        else format = _format;

        li = LayoutInflater.from(context);
        addTitleView(_containerName);

        checkBoxNames = new String[_intData.length];

        for (int i = 0; i < _intData.length; i++)
            checkBoxNames[i] = String.valueOf(_intData[i]);

        setData(checkBoxNames);

    }

    @Override
    public void setSelected(int index) {
        RadioButton castView = (RadioButton) container.findViewWithTag(index);
        if (castView != null) {
            castView.performClick();
        }
    }


    private void setData(final String[] _checkBoxNames) {
        for (int i = 0; i < _checkBoxNames.length; i++) {
            String text = _checkBoxNames[i] + " " + format;
            addCheckBox(text, i);
        }
    }


    private void addCheckBox(String text, int id) {
        RadioButton cb = (RadioButton) li.inflate(R.layout.checkbox,null);

        cb.setText(text);
        cb.setTag(id);
        cb.setOnClickListener(this);
        cb.setHeight(context.getResources().getDimensionPixelSize(R.dimen.cb_height));
         container.addView(cb);
    }


    private void addTitleView(String title) {
        TextView titleView = (TextView) li.inflate(R.layout.container_title, null);
        titleView.setText(title);

        container.addView(titleView);
    }


    @Override
    public void onClick(View view) {
        int id = (Integer) (view.getTag());
        callback.setDataIndex(id);
        RadioButton castView = (RadioButton) view;

        if (castView.isChecked()) {

            for (int i = 0; i < checkBoxNames.length; i++)
                if (i != id) {
                    ((RadioButton) container.findViewWithTag(i)).setChecked(false);
                }
        } else {
            castView.setChecked(true);
        }
    }

    @Override
    public RadioGroupPresenter.RadioGroupState saveState () {
        boolean[] checkedState = new boolean[checkBoxNames.length];
        for (int i = 0; i < checkBoxNames.length; i++) {
            checkedState[i] = ((RadioButton) container.findViewWithTag(i)).isChecked();
        }
        return new RadioGroupState(checkedState);
    }

    @Override
    public void restoreState (RadioGroupState state) {
        if(state != null){
            for (int i = 0; i < state.state.length; i++) {
               if( state.state[i]){
                   setSelected(i);
                   break;
               }
            }
        }
    }
    public static class RadioGroupState implements Parcelable {
        boolean [] state;

        public RadioGroupState (boolean[] state) {
            this.state = state;
        }

        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel dest, int flags) {
            dest.writeBooleanArray(this.state);
        }

        public RadioGroupState () {
        }

        protected RadioGroupState (Parcel in) {
            this.state = in.createBooleanArray();
        }

        public static final Parcelable.Creator<RadioGroupState> CREATOR = new Parcelable.Creator<RadioGroupState>() {
            @Override
            public RadioGroupState createFromParcel (Parcel source) {
                return new RadioGroupState(source);
            }

            @Override
            public RadioGroupState[] newArray (int size) {
                return new RadioGroupState[size];
            }
        };
    }
}
