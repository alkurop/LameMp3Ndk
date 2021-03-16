package com.omar.retromp3recorder.app.modules

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.modules.share.Sharer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import java.io.File
import javax.inject.Inject
import javax.inject.Named

open class TestSharingModule @Inject internal constructor(
    @param:Named(MockModule.SHARING_SUBJECT) private val events: Subject<Sharer.Event>
) :
    Sharer {
    override fun share(file: File): Completable {

        return Completable.fromAction { events.onNext(Sharer.Event.SharingOk(Stringer.ofString("test"))) }
    }

    override fun observeEvents(): Observable<Sharer.Event> {
        return events
    }
}
