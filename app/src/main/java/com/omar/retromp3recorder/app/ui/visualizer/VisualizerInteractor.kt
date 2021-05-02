package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.PlayerIdRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class VisualizerInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val playerIdRepo: PlayerIdRepo,
    private val audioStateMapper: AudioStateMapper
) {
    fun processIO(): ObservableTransformer<VisualizerView.Input, VisualizerView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<VisualizerView.Output> = {
        Observable.merge(listOf(
            audioStateMapper.observe()
                .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdRepo.observe()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) }
        ))
    }
}