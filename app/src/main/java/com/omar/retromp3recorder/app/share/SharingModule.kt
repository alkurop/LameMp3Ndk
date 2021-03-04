package com.omar.retromp3recorder.app.share

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.Completable
import io.reactivex.Observable

interface SharingModule {
    fun share(): Completable
    fun observeEvents(): Observable<Event>

    sealed class Event {
        data class SharingOk internal constructor(val message: Stringer) : Event()
        data class SharingError internal constructor(val error: Stringer) : Event()
    }
}
