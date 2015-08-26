package com.omar.retromp3recorder.app.utils;

import android.content.Context;

/**
 * Created by omar on 17.08.15.
 */
public class ContextHelper {

    private static Context mContext;

    public static void SetContext(Context context) {
        mContext = context;
    }

    public static Context GetContext() {
        return mContext;
    }
}

