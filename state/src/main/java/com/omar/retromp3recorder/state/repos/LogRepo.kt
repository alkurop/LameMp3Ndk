package com.omar.retromp3recorder.state.repos

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Observable
import javax.inject.Inject

class LogRepo @Inject constructor(
    audioPlayer: AudioPlayer,
    recorder: Mp3VoiceRecorder,
    sharingModule: com.omar.retromp3recorder.share.Sharer

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

private fun Mp3VoiceRecorder.createLogs(): Observable<LogRepo.Event> {
    val message = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Message::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }

    val error = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Error::class.java)
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

private fun com.omar.retromp3recorder.share.Sharer.createLogs(): Observable<LogRepo.Event> {
    val message = this
        .observeEvents()
        .ofType(com.omar.retromp3recorder.share.Sharer.Event.SharingOk::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }

    val error = this
        .observeEvents().ofType(com.omar.retromp3recorder.share.Sharer.Event.SharingError::class.java)
        .map { answer -> LogRepo.Event.Error(answer.error) }
    return Observable.merge(message, error)
}