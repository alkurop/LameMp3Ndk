package com.omar.retromp3recorder.state.repos

sealed class AudioState {
    data class Idle(val hasFile: Boolean) : AudioState()
    object Playing : AudioState()
    object Recording : AudioState()
}