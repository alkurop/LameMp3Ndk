package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeProgress
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.SeekRepo
import com.omar.retromp3recorder.utils.AudioDurationRetriever
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.SeekbarTime
import com.omar.retromp3recorder.utils.toSeekbarTime
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val audioDurationRetriever: AudioDurationRetriever,
    private val currentFileRepo: CurrentFileRepo,
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val seekRepo: SeekRepo
) {
    fun observe(): Observable<Optional<Pair<SeekbarTime, SeekbarTime>>> {
        return audioPlayer.observeProgress()
            .distinctUntilChanged()
            .map { (position, duration) ->
                val mappedPosition = position.toSeekbarTime()
                val mappedDuration = duration.toSeekbarTime()
                seekRepo.onNext(Optional(if (mappedDuration == mappedPosition) 0 else mappedPosition))
                Optional(Pair(mappedPosition, mappedDuration))
            }
            .mergeWith(
                Observable.combineLatest(
                    hasPlayableFileMapper.observe(),
                    currentFileRepo.observe(), { isFilePlayable, currentFile ->
                        if (!isFilePlayable) Optional.empty()
                        else {
                            val durationMillis =
                                audioDurationRetriever.getAudioDurationForExistingFile(currentFile.value!!)
                            val ourDuration = durationMillis.toSeekbarTime()
                            Optional(0 to ourDuration)
                        }
                    })
            )
            .distinctUntilChanged()
    }
}