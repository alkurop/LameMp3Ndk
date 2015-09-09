package com.omar.retromp3recorder.app.views;

import android.widget.LinearLayout;

/**
 * Created by omar on 17.08.15.
 */
public interface IMainView {

    void setUI();

    void setRecordBtnImg(int drawable);

    void setPlayBtnImg(int drawable);

    void startVisualizer(int playerId);

    void stopVisualizer();

    void setLabelText(String s);

    LinearLayout getRadioContainer1();

    LinearLayout getRadioContainer2();

}
