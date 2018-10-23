package com.omar.retromp3recorder.app.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.stringer.Stringer;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static com.omar.retromp3recorder.app.di.AppComponent.MAIN_THREAD;

public final class SharingModuleRX implements SharingModule {

    private final SharebleFileUriCreator sharebleFileUriCreator;
    private final Context context;
    private final FileNameRepo fileNameRepo;
    private final Stringer stringer;
    private final Scheduler scheduler;
    private final Scheduler mainThreadScheduler;
    private final PublishSubject<Event> events = PublishSubject.create();

    @Inject
    SharingModuleRX(
            SharebleFileUriCreator sharebleFileUriCreator,
            Context context,
            FileNameRepo fileNameRepo,
            Stringer stringer,
            Scheduler scheduler,
            @Named(MAIN_THREAD) Scheduler mainThreadScheduler
    ) {
        this.sharebleFileUriCreator = sharebleFileUriCreator;
        this.context = context;
        this.fileNameRepo = fileNameRepo;
        this.stringer = stringer;
        this.scheduler = scheduler;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @Override
    public Completable share() {
        return fileNameRepo.observe().take(1)
                .flatMapCompletable((Function<String, Completable>) fileName ->
                        {
                            File file = new File(fileName);
                            if (!file.exists()) {
                                return Completable
                                        .fromAction(() ->
                                                events.onNext(
                                                        new SharingError(stringer.getString(R.string.trying_to_share)))
                                        );
                            } else {
                                return Single
                                        .fromCallable(() -> {
                                            Uri uri = collectForShare(file);
                                            return initShareIntent(uri);
                                        })
                                        .subscribeOn(scheduler)
                                        .flatMapCompletable(intent -> Completable.fromAction(() ->
                                                context.startActivity(
                                                        Intent
                                                                .createChooser(
                                                                        intent,
                                                                        stringer.getString(R.string.select)
                                                                )
                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))))
                                        .subscribeOn(mainThreadScheduler)
                                        .andThen(Completable.fromAction(() ->
                                                events.onNext(
                                                        new SharingOk(stringer.getString(R.string.trying_to_share))
                                                ))
                                        );
                            }
                        }
                );
    }

    @Override
    public Observable<Event> observeEvents() {
        return events;
    }

    @SuppressLint("SetWorldReadable")
    private Uri collectForShare(File file) {
        boolean success = file.setReadable(true, false);
        return sharebleFileUriCreator.createSharableUri(file);
    }

    private Intent initShareIntent(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
