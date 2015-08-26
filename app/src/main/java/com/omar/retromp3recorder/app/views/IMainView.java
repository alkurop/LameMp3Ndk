package com.omar.retromp3recorder.app.views;

import android.widget.LinearLayout;

/**
 * Created by omar on 17.08.15.
 */
public interface IMainView {

     void SetUI();

    void SetRecordBtnImg(int drawable);

    void  SetPlayBtnImg(int drawable);

    void StartVisualizer(int playerId);

    void StopVisualizer();

    void SetLabelText(String s);

    LinearLayout GetRadioContainer1();

    LinearLayout GetRadioContainer2();

}
