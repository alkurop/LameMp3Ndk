package com.omar.retromp3recorder.app.files;

import android.content.Context;

import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import java.io.File;

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
        String fileName = VOICE_RECORD + "_" + String.valueOf(System.currentTimeMillis()) + MP3_EXTENSION;
        return getFileDir() + "/" + fileName;
    }

    public String getFileDir() {
        return context.getExternalCacheDir().toString();
    }
}
