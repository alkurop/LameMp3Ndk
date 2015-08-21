package com.omar.lamemp3ndk.app.utils;

import com.omar.lamemp3ndk.app.Constants;

/**
 * Created by omar on 21.08.15.
 */
public class TouchController {
    private static boolean isAllowClick = true;

    public static boolean allowClick() {

        if (isAllowClick) new Thread(() -> {
           {
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

     interface LL {
         void onCLick();
         void onFUck();
     }



}
