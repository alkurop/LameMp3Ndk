package com.omar.retromp3recorder.app.presenters;

import android.widget.LinearLayout;

import com.omar.retromp3recorder.app.callbacks.ICheckboxCallback;

/**
 * Created by omar on 20.08.15.
 */
public interface IRadioGroupEvents {

    void createRadioGroup(final LinearLayout _container, final String _containerName, final String _format, final int[] _intData, final ICheckboxCallback _callback);

    void setSelected(int index);

    RadioGroupPresenter.RadioGroupState saveState();

    void restoreState (RadioGroupPresenter.RadioGroupState state);
}
