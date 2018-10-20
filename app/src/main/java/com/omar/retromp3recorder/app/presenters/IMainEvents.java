package com.omar.retromp3recorder.app.presenters;

import android.os.Parcelable;

import com.omar.retromp3recorder.app.main.IMainView;

/**
 * Created by omar on 17.08.15.
 */
public interface IMainEvents {

    void init(IMainView _view);

    void recordClicked();

    void playClicked();

    void shareClicked();

    void stopAll();

    Parcelable saveState();

    void restoreState(Parcelable parcelable);

    void hardfixRestoreState ();
}
