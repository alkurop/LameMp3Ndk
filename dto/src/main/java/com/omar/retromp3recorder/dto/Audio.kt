package com.omar.retromp3recorder.dto

import com.github.alkurop.ghostinshell.Shell

sealed class JoinedProgress {
    object Hidden : JoinedProgress()
    data class RecorderProgressShown(
        val progress: Long,
        val wavetable: Wavetable
    ) : JoinedProgress()

    data class PlayerProgressShown(
        val progress: Shell<PlayerProgress>,
        val wavetable: Shell<Wavetable>
    ) : JoinedProgress()
}

data class Wavetable(
    val data: ByteArray,
    val stepMillis: Int
)

data class PlayerProgress(
    val progress: Long,
    val duration: Long
)

