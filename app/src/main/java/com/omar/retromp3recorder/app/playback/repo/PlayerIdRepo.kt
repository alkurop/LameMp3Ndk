package com.omar.retromp3recorder.app.playback.repo

import com.omar.retromp3recorder.app.utils.OneShot
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerIdRepo @Inject internal constructor() {
    private val events: Subject<OneShot<Int>> = ReplaySubject.create()
    fun observe(): Observable<OneShot<Int>> {
        return events
    }

    fun newValue(event: Int) {
        events.onNext(OneShot(event))
    }
}
