package com.omar.retromp3recorder.app.utils;

import android.content.Context;
import android.support.annotation.StringRes;

@NotUnitTestable
public class ContextStringer implements Stringer {
    private final Context context;

    public ContextStringer(Context context) {
        this.context = context;
    }

    @Override
    public String getString(@StringRes int stringRes){
        return context.getString(stringRes);
    }

    @Override
    public String getString(@StringRes int stringRes, Object ... args){
        return context.getString(stringRes, args);
    }
}
