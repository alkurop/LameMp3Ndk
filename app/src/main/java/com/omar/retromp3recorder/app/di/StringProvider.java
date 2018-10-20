package com.omar.retromp3recorder.app.di;

import android.support.annotation.StringRes;

public interface StringProvider {
    String getString(@StringRes int strintRes);

    String getString(@StringRes int stringRes, Object... args);
}
