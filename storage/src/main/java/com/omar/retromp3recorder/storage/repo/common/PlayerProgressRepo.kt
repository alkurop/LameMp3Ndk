package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.toPlayerTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerProgressRepo @Inject constructor() :
    ReducerRepo<PlayerProgressRepo.In, PlayerProgressRepo.Out>(
        init = Out.Hidden,
        function = FUNCTION
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

private fun PlayerProgressRepo.In.Progress.map() = PlayerProgressRepo.Out.Shown(
    progress = this.progress,
    duration = this.duration,
    timestamp = System.currentTimeMillis()
)

private val FUNCTION: PlayerProgressRepo.Out.(PlayerProgressRepo.In) -> PlayerProgressRepo.Out =
    { input ->
        when (input) {
            is PlayerProgressRepo.In.Seek -> (this as PlayerProgressRepo.Out.Shown).copy(
                progress = input.progress.toPlayerTime()
            )
            is PlayerProgressRepo.In.Progress -> input.map()
            else -> PlayerProgressRepo.Out.Hidden
        }
    }
