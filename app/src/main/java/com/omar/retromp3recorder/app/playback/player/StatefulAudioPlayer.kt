package com.omar.retromp3recorder.app.playback.player

import io.reactivex.Observable

interface StatefulAudioPlayer {

    fun observeState(): Observable<State>

    enum class State {
        Idle,
        Playing
    }
}