package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.state.repos.AudioStateRepo
import com.omar.retromp3recorder.state.repos.PlayerIdRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class VisualizerInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val playerIdRepo: PlayerIdRepo,
    private val audioStateRepo: AudioStateRepo
) {
    fun processIO(): ObservableTransformer<VisualizerView.Input, VisualizerView.Output> =
        ObservableTransformer { upstream ->
            upstream.observeOn(scheduler)
                .compose { actions ->
                    Observable.merge(
                        actionsMapper(actions).toObservable(),
                        repoMapper()
                    )
                }
        }

    private fun actionsMapper(actions: Observable<VisualizerView.Input>) = Completable.merge(listOf())

    private fun repoMapper(): Observable<VisualizerView.Output> = Observable.merge(listOf(
        audioStateRepo.observe()
            .map { state -> VisualizerView.Output.StateChangedOutput(state) },
        playerIdRepo.observe()
            .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) }
    ))
}