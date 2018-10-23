package com.omar.retromp3recorder.app;

import android.media.AudioFormat;

/**
 * Created by omar on 19.08.15.
 */
public class Constants {

    public static final String KBPS = "kbps";
    public static final String HZ = "Hz";
    public static final String KB = "kb";
    public static final String MP3_EXTENTION = ".mp3";


    public static final String VOICE_RECORD = "voice_record";
    public static final String MP3_LAME_LIB = "mp3lame";

    public static final short[] AUDIO_FORMAT_PRESETS = new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat
            .ENCODING_PCM_16BIT};


    public static final int[] QUALITY_PRESETS = new int[]{2, 5, 7};  // the lower the better
    public static final short[] CHANNEL_PRESETS = new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat
            .CHANNEL_IN_STEREO};

}

