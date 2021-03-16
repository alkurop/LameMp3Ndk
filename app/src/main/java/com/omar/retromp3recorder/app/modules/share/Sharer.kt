package com.omar.retromp3recorder.app.modules.share

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File

interface Sharer {
    fun share(file: File): Completable
    fun observeEvents(): Observable<Event>

    sealed class Event {
        data class SharingOk internal constructor(val message: Stringer) : Event()
        data class SharingError internal constructor(val error: Stringer) : Event()
    }
}
