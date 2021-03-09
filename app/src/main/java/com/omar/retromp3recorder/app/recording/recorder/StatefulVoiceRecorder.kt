package com.omar.retromp3recorder.app.recording.recorder

import io.reactivex.Observable

interface StatefulVoiceRecorder {

    fun observeState(): Observable<State>

    enum class State {
        Idle,
        Recording
    }
}