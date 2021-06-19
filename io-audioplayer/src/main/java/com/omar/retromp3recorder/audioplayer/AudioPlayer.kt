package com.omar.retromp3recorder.audioplayer

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.rxjava3.core.Observable

interface AudioPlayer {
    fun observe(): Observable<Output>
    fun observeState(): Observable<State>

    fun onInput(input: Input)

    sealed class Input {
        data class Start(val options: PlayerStartOptions) : Input()
        object Stop : Input()
        object SeekPause : Input()
        object Resume : Input()
        data class Seek(val position: Long) : Input()
    }

    sealed class Output {
        data class Progress(val position: Long, val duration: Long) : Output()

        sealed class Event : Output() {
            data class Message(val message: Stringer) : Event()
            data class Error(val error: Stringer) : Event()
            data class AudioSessionId constructor(val playerId: Int) : Event()
        }
    }

    enum class State {
        Idle,
        Playing,
        Seek_Paused
    }
}

data class PlayerStartOptions(
    val seekPosition: Long? = null,
    val filePath: String
)

fun AudioPlayer.observeEvents(): Observable<AudioPlayer.Output.Event> =
    this.observe().ofType(AudioPlayer.Output.Event::class.java)

fun AudioPlayer.observeProgress(): Observable<AudioPlayer.Output.Progress> =
    this.observe().ofType(AudioPlayer.Output.Progress::class.java)