package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.toPlayerTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerProgressRepo @Inject constructor() :
    ReducerRepo<PlayerProgressRepo.In, Optional<PlayerProgress>>(
        init = Optional.empty(),
        function = FUNCTION
    ) {
    sealed class In {
        data class Seek(
            val progress: Int,
        ) : In()

        data class Progress(val progress: PlayerProgress) : In()
        object Hidden : In()
    }
}

private val FUNCTION: Optional<PlayerProgress>.(PlayerProgressRepo.In) -> Optional<PlayerProgress> =
    { input ->
        when (input) {
            is PlayerProgressRepo.In.Seek -> Optional(
                this.value!!.copy(
                    progress = input.progress.toPlayerTime()
                )
            )
            is PlayerProgressRepo.In.Progress -> Optional(input.progress)
            else -> Optional.empty()
        }
    }
