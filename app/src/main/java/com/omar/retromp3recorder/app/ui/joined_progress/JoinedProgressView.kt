package com.omar.retromp3recorder.app.ui.joined_progress

object JoinedProgressView {

    sealed class In {
        data class SeekToPosition(val position: Int) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }
}