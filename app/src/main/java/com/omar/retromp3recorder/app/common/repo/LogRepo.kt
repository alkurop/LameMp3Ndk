package com.omar.retromp3recorder.app.common.repo

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.share.Sharer
import io.reactivex.Observable
import javax.inject.Inject

class LogRepo @Inject constructor(
    audioPlayer: AudioPlayer,
    recorder: VoiceRecorder,
    sharingModule: Sharer

) {
    private val observable: Observable<Event> = Observable.merge(
        audioPlayer.createLogs(),
        recorder.createLogs(),
        sharingModule.createLogs()
    ).share()

    fun observe(): Observable<Event> = observable

    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
    }
}

private fun VoiceRecorder.createLogs(): Observable<LogRepo.Event> {
    val message = this
        .observeEvents()
        .ofType(VoiceRecorder.Event.Message::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }

    val error = this
        .observeEvents()
        .ofType(VoiceRecorder.Event.Error::class.java)
        .map { answer -> LogRepo.Event.Error(answer.error) }
    return Observable.merge(message, error)
}

private fun AudioPlayer.createLogs(): Observable<LogRepo.Event> {
    val message: Observable<LogRepo.Event> = this
        .observeEvents()
        .ofType(AudioPlayer.Event.Message::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }

    val error: Observable<LogRepo.Event> = this
        .observeEvents()
        .ofType(AudioPlayer.Event.Error::class.java)
        .map { answer -> LogRepo.Event.Error(answer.error) }

    return Observable.merge(message, error)
}

private fun Sharer.createLogs(): Observable<LogRepo.Event> {
    val message = this
        .observeEvents()
        .ofType(Sharer.Event.SharingOk::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }
    val error = this
        .observeEvents().ofType(Sharer.Event.SharingError::class.java)
        .map { answer -> LogRepo.Event.Error(answer.error) }
    return Observable.merge(message, error)
}