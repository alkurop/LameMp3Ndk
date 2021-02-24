package com.omar.retromp3recorder.app.common.repo

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepo @Inject constructor() {
    private val events: Subject<Event> = ReplaySubject.create()
    fun observe(): Observable<Event> {
        return events
    }

    fun newValue(event: Event) {
        events.onNext(event)
    }

    sealed class Event {
        data class Message(val message: String) : Event()
        data class Error(val error: String) : Event()
    }
}