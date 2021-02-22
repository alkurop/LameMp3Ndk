package com.omar.retromp3recorder.app.share;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@NotUnitTestable
public class SharableFileUriCreator {

    private final Context context;

    @Inject
    SharableFileUriCreator(Context context) {
        this.context = context;
    }

    Uri createSharableUri(File file) {
        return FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() +
                        ".com.omar.retromp3recorder.app.provider",
                file);

    }
}
