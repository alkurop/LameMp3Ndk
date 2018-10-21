package com.omar.retromp3recorder.app.recorder;

import android.content.Context;

import com.omar.retromp3recorder.app.Constants;

import javax.inject.Inject;

public class FilePathGenerator {
    private final Context context;

    @Inject
    public FilePathGenerator(Context context) {
        this.context = context;
    }

    public String genrateFilePath() {
        String name = Constants.VOICE_RECORD;
        String fileName = name + Constants.MP3_EXTENTION;
        return context.getExternalCacheDir() + "/" + fileName;
    }
}
