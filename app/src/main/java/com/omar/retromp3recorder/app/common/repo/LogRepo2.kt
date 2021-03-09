package com.omar.retromp3recorder.app.common.repo

import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.share.SharingModule
import io.reactivex.Observable
import javax.inject.Inject

class LogRepo2 @Inject constructor(
    audioPlayer: AudioPlayer,
    recorder: VoiceRecorder,
    sharingModule: SharingModule

) {
    private val observable: Observable<LogRepo.Event> = Observable.merge(
        audioPlayer.createLogs(),
        recorder.createLogs(),
        sharingModule.createLogs()
    ).share()

    fun observe(): Observable<LogRepo.Event> = observable
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

private fun SharingModule.createLogs(): Observable<LogRepo.Event> {
    val message = this
        .observeEvents()
        .ofType(SharingModule.Event.SharingOk::class.java)
        .map { answer -> LogRepo.Event.Message(answer.message) }
    val error = this
        .observeEvents().ofType(SharingModule.Event.SharingError::class.java)
        .map { answer -> LogRepo.Event.Error(answer.error) }
    return Observable.merge(message, error)
}