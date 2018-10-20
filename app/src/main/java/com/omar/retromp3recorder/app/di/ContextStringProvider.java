package com.omar.retromp3recorder.app.di;

import android.content.Context;
import android.support.annotation.StringRes;

public class ContextStringProvider implements StringProvider {
    private final Context context;

    public ContextStringProvider(Context context) {
        this.context = context;
    }

    @Override
    public String getString(@StringRes int strintRes){
        return context.getString(strintRes);
    }

    @Override
    public String getString(@StringRes int stringRes, Object ... args){
        return context.getString(stringRes, args);
    }
}
