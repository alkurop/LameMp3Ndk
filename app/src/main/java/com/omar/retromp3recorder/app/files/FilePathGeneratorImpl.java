package com.omar.retromp3recorder.app.files;

import android.content.Context;

import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import java.io.File;

import javax.inject.Inject;

@NotUnitTestable
public class FilePathGeneratorImpl implements FilePathGenerator {
    private static final String MP3_EXTENSION = ".mp3";
    private static final String VOICE_RECORD = "voice_record";

    private final Context context;

     public FilePathGeneratorImpl(Context context) {
        this.context = context;
    }

    @Override
    public String generateFilePath() {
        String fileName = VOICE_RECORD + "_" + String.valueOf(System.currentTimeMillis()) + MP3_EXTENSION;
        return getFileDir() + "/" + fileName;
    }

    @Override
    public String getFileDir() {
        return context.getExternalCacheDir().toString();
    }
}
