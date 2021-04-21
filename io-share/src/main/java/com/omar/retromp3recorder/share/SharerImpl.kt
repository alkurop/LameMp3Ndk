package com.omar.retromp3recorder.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.share.Sharer.Event.Error
import com.omar.retromp3recorder.share.Sharer.Event.SharingOk
import com.omar.retromp3recorder.utils.FileUriCreator
import com.omar.retromp3recorder.utils.MAIN_THREAD
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class SharerImpl @Inject internal constructor(
    private val fileUriCreator: FileUriCreator,
    private val context: Context,
    @param:Named(MAIN_THREAD) private val mainThreadScheduler: Scheduler
) : Sharer {
    private val events = PublishSubject.create<Sharer.Event>()
    override fun share(file: File): Completable {
        return if (!file.exists()) {
            Completable.fromAction {
                events.onNext(Error(Stringer(R.string.sh_trying_to_share)))
            }
        } else Completable
            .fromAction {
                val uri = collectForShare(file)
                val intent = initShareIntent(uri)
                context.startActivity(
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.sh_select)
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                SharingOk(Stringer(R.string.sh_trying_to_share))
            }

            .subscribeOn(mainThreadScheduler)
    }

    override fun observeEvents(): Observable<Sharer.Event> {
        return events
    }

    @SuppressLint("SetWorldReadable")
    private fun collectForShare(file: File): Uri {
        file.setReadable(true, false)
        return fileUriCreator.createSharableUri(file)
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
