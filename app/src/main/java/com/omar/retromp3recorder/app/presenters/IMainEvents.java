package com.omar.retromp3recorder.app.presenters;

import com.omar.retromp3recorder.app.views.IMainView;

/**
 * Created by omar on 17.08.15.
 */
public interface IMainEvents {

    void init(IMainView _view);

    void recordClicked();

    void playClicked();

    void shareClicked();

    void stopAll();
}
