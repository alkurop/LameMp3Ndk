package com.omar.retromp3recorder.audioplayer

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.rxjava3.core.Observable

interface AudioPlayer {
    fun playerStop()
    fun playerStart(options: PlayerStartOptions)
    fun seek(position: Long)

    fun observeEvents(): Observable<Event>
    val isPlaying: Boolean

    //region events
    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
        data class AudioSessionId constructor(val playerId: Int) : Event()
    }

    fun observeState(): Observable<State>

    enum class State {
        Idle,
        Playing
    }

    fun observerProgress(): Observable<Pair<Long, Long>>
}

private const val PROGRESS_CONVERSION_RATE = 100

fun Int.toPlayerTime(): Long = this.toLong() * PROGRESS_CONVERSION_RATE
fun Long.toSeekbarTime(): Int = (this / PROGRESS_CONVERSION_RATE).toInt()

data class PlayerStartOptions(
    val seekPosition: Long? = null,
    val filePath: String
)