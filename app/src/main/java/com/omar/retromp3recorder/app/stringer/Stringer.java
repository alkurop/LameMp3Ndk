package com.omar.retromp3recorder.app.stringer;

import android.support.annotation.StringRes;

public interface Stringer {
    String getString(@StringRes int stringRes);

    String getString(@StringRes int stringRes, Object... args);
}