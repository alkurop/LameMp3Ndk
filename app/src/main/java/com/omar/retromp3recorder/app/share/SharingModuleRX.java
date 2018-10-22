package com.omar.retromp3recorder.app.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.stringer.Stringer;
import com.omar.retromp3recorder.app.repo.FileNameRepo;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public final class SharingModuleRX implements SharingModule {
    private final Context context;
    private final FileNameRepo fileNameRepo;
    private final Stringer stringer;
    private final Scheduler scheduler;
    private final PublishSubject<Event> events = PublishSubject.create();

    @Inject
    public SharingModuleRX(
            Context context,
            FileNameRepo fileNameRepo,
            Stringer stringer,
            Scheduler scheduler
    ) {
        this.context = context;
        this.fileNameRepo = fileNameRepo;
        this.stringer = stringer;
        this.scheduler = scheduler;
    }

    @Override
    public Completable share() {
        return fileNameRepo.observe().take(1)
                .flatMapCompletable((Function<String, Completable>) fileName ->
                        {
                            File file = new File(fileName);
                            if (!file.exists()) {
                                return
                                        Completable.fromAction(() ->
                                                events.onNext(
                                                        new SharingError(stringer.getString(R.string.trying_to_share)))
                                        );
                            } else {
                                return
                                        Single
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
                                                .subscribeOn(mainThread())
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

    private Uri collectForShare(File file) {
        file.setReadable(true, false);
        return Uri.fromFile(file);
    }

    private Intent initShareIntent(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("audio/mpeg4-generic");
        return shareIntent;
    }
}
