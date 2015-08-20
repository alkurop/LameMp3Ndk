package com.omar.lamemp3ndk.app;

import android.media.AudioFormat;

/**
 * Created by omar on 19.08.15.
 */
public class Constants {

    public static final int[] SAMPLE_RATE_PRESETS = new int[]{44100, 22050,  11025, 8000};


    public static final  short[] AUDIO_FORMAT_PRESETS = new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat
            .ENCODING_PCM_16BIT};

    public static final String [] AUDIO_FOMRATS = new String []{"ENCODING_PCM_8BIT", "ENCODING_PCM_16BIT"};

    public static final int[] QUALITY_PRESETS = new int[]{2, 5, 7};  // the lower the better
    public static final  short[] CHANNEL_PRESETS = new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat
            .CHANNEL_IN_STEREO};
    public static final String [] CHANNEL_LABELS = new String [] {"CHANNEL_IN_MONO", "CHANNEL_IN_STEREO"};


    public static final String BPM_LABEL = "bpm";
    public static final String HZ_LABEL = "hz";

    public static final String BIT_RATE_LABEL = "Bit Rate";
    public static final String SAMPLE_RATE_LABEL = "Sample rate";

    public static final int[] BIT_RATE_PRESETS = new int[]{320, 192, 160, 128};



}
