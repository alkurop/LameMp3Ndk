package com.omar.retromp3recorder.app.share

import io.reactivex.Completable
import io.reactivex.Observable

interface SharingModule {
    fun share(): Completable
    fun observeEvents(): Observable<Event>

    sealed class Event {
        data class SharingOk internal constructor(val message: String) : Event()
        data class SharingError internal constructor(val error: String) : Event()
    }
}
