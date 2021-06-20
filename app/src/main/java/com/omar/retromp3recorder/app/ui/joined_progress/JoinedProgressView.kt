package com.omar.retromp3recorder.app.ui.joined_progress

object JoinedProgressView {
    sealed class Out {
        object NothingToShow : Out()
        data class PlayerProgress(
            val progress: PlayerProgress
        ) : Out()

        object RecorderProgress : Out()
    }

    sealed class In {
        data class SeekToPosition(val position: Int) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }
}