package com.omar.retromp3recorder.app.share

import com.omar.retromp3recorder.app.di.MockModule
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

open class TestSharingModule @Inject internal constructor(
    @param:Named(MockModule.SHARING_SUBJECT) private val events: Subject<SharingModule.Event>
) :
    SharingModule {
    override fun share(): Completable {
        return Completable.complete()
    }

    override fun observeEvents(): Observable<SharingModule.Event> {
        return events
    }
}
