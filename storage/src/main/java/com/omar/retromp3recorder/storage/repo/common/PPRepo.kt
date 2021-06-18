package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.toPlayerTime

class PPRepo : ReducerRepo<PPRepo.In, PPRepo.Out>(
    init = Out.Hidden,
    function = function
) {
    sealed class In {
        data class Seek(
            val progress: Int,
            val timestamp: Long = System.currentTimeMillis()
        ) : In()

        data class Progress(
            val progress: Long,
            val duration: Long,
            val timestamp: Long = System.currentTimeMillis()
        ) : In()

        object Hidden : In()
    }

    sealed class Out {
        object Hidden : Out()
        data class Shown(
            val progress: Long,
            val duration: Long,
            val timestamp: Long
        ) : Out()
    }
}

private fun PPRepo.In.Progress.map() = PPRepo.Out.Shown(
    progress = this.progress,
    duration = this.duration,
    timestamp = System.currentTimeMillis()
)

private val function: PPRepo.Out.(PPRepo.In) -> PPRepo.Out = { input ->
    when (input) {
        is PPRepo.In.Seek -> (this as PPRepo.Out.Shown).copy(
            progress = input.progress.toPlayerTime()
        )
        is PPRepo.In.Progress -> input.map()
        else -> PPRepo.Out.Hidden
    }
}
