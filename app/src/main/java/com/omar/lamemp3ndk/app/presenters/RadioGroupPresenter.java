package com.omar.lamemp3ndk.app.presenters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.omar.lamemp3ndk.app.R;
import com.omar.lamemp3ndk.app.callbacks.CheckboxCallback;

/**
 * Created by omar on 19.08.15.
 */
public class RadioGroupPresenter implements IRadioGroupEvents, View.OnClickListener {

    private LinearLayout container;
    private String format;
    private String[] checkBoxNames;
    private CheckboxCallback callback;
    private LayoutInflater li;
    private Context context;





    @Override
    public void CreateRadioGroup(final LinearLayout _container, final String _containerName, final String _format, final int[] _intData, final CheckboxCallback _callback) {

        container = _container;
        context = _container.getContext();
        callback = _callback;

        if (_format == null) format = "";
                else  format = _format;


        li = LayoutInflater.from(context);
        addTitleView(_containerName);

        checkBoxNames = new String[_intData.length];
        for (int i = 0; i < _intData.length; i++)
            checkBoxNames[i] = String.valueOf(_intData[i]);
        setData(checkBoxNames);

    }

    @Override
    public void SetSelected(int index) {

        RadioButton castView = (RadioButton) container.findViewWithTag(index);
        if(castView != null ) {
            castView.performClick();
        }
    }


    private void setData(final String[] _checkBoxNames) {

        for (int i = 0; i < _checkBoxNames.length; i++) {
            String text = _checkBoxNames[i] + " " + format;
            addCheckBox(text, i );
        }
    }


    private void addCheckBox(String text, int id ) {
        RadioButton cb = (RadioButton) li.inflate(R.layout.checkbox, null);
        cb.setText(text);
        cb.setTag(id);
        cb.setOnClickListener(this);
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
        }else{castView.setChecked(true);}

    }
}
