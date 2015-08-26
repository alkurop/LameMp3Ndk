package com.omar.retromp3recorder.app.presenters;

import com.omar.retromp3recorder.app.views.IMainView;

/**
 * Created by omar on 17.08.15.
 */
public interface IMainEvents {

    void  Init(IMainView _view);

    void RecordClicked();

    void PlayClicked();

    void ShareClicked();

    void StopAll();
}
