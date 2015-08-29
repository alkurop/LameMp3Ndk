package com.omar.retromp3recorder.app.utils;

import com.omar.retromp3recorder.app.Constants;

/**
 * Created by omar on 21.08.15.
 */
public class TouchController {
    private static boolean isAllowClick = true;

    public static boolean allowClick() {

        if (isAllowClick) new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isAllowClick = false;
                    Thread.sleep(Constants.MILLI_DELAY);
                    isAllowClick = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    return isAllowClick;
    }
}
