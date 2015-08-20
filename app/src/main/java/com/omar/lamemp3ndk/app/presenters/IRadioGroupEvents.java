package com.omar.lamemp3ndk.app.presenters;

import android.widget.LinearLayout;
import com.omar.lamemp3ndk.app.callbacks.CheckboxCallback;

/**
 * Created by omar on 20.08.15.
 */
public interface IRadioGroupEvents {


    void CreateRadioGroup(final LinearLayout _container, final String _containerName, final String _format, final
    int[] _intData, final CheckboxCallback _callback);


    void SetSelected(int index);
}
