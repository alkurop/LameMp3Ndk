package com.omar.retromp3recorder.app.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import com.omar.retromp3recorder.app.share.Sharer.Event.SharingError
import com.omar.retromp3recorder.app.share.Sharer.Event.SharingOk
import com.omar.retromp3recorder.app.utils.NotUnitTestable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@NotUnitTestable
class SharerIO @Inject internal constructor(
    private val sharableFileUriCreator: SharableFileUriCreator,
    private val context: Context,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler,
    @param:Named(AppComponent.MAIN_THREAD) private val mainThreadScheduler: Scheduler
) : Sharer {
    private val events = PublishSubject.create<Sharer.Event>()
    override fun share(): Completable {
        return currentFileRepo.observe().take(1)
            .flatMapCompletable { fileName ->
                val file = File(fileName)
                if (!file.exists()) {
                    Completable.fromAction {
                        events.onNext(SharingError(Stringer(R.string.trying_to_share)))
                    }
                } else Single
                    .fromCallable {
                        val uri = collectForShare(file)
                        initShareIntent(uri)
                    }
                    .subscribeOn(scheduler)
                    .flatMapCompletable { intent ->
                        Completable
                            .fromAction {
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        context.getString(R.string.select)
                                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            }
                    }
                    .subscribeOn(mainThreadScheduler)
                    .andThen(
                        Completable.fromAction {
                            events.onNext(
                                SharingOk(Stringer(R.string.trying_to_share))
                            )
                        }
                    )
            }
    }

    override fun observeEvents(): Observable<Sharer.Event> {
        return events
    }

    @SuppressLint("SetWorldReadable")
    private fun collectForShare(file: File): Uri {
        val success = file.setReadable(true, false)
        return sharableFileUriCreator.createSharableUri(file)
    }

    private fun initShareIntent(uri: Uri): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "audio/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return intent
    }
}
