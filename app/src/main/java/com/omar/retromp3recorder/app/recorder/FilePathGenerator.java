package com.omar.retromp3recorder.app.recorder;

import android.content.Context;

import com.omar.retromp3recorder.app.Constants;
import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import javax.inject.Inject;

@NotUnitTestable
public class FilePathGenerator {
    private final Context context;

    @Inject
    public FilePathGenerator(Context context) {
        this.context = context;
    }

    public String generateFilePath() {
        String name = Constants.VOICE_RECORD;
        String fileName = name + Constants.MP3_EXTENTION;
        return context.getExternalCacheDir() + "/" + fileName;
    }
}
