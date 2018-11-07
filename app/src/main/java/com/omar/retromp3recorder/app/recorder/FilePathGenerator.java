package com.omar.retromp3recorder.app.recorder;

import android.content.Context;

import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import javax.inject.Inject;

@NotUnitTestable
public class FilePathGenerator {
    private static final String MP3_EXTENSION = ".mp3";
    private static final String VOICE_RECORD = "voice_record";

    private final Context context;

    @Inject
    public FilePathGenerator(Context context) {
        this.context = context;
    }

    public String generateFilePath() {
        String fileName = VOICE_RECORD + MP3_EXTENSION;
        return context.getExternalCacheDir() + "/" + fileName;
    }
}
