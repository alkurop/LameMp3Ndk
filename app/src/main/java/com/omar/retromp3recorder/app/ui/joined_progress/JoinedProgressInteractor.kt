package com.omar.retromp3recorder.app.ui.joined_progress
//class ProgressInteractor @Inject constructor(
//    private val audioSeekProgressUC: AudioSeekProgressUC,
//    private val audioSeekPauseUC: AudioSeekPauseUC,
//    private val audioSeekFinishUC: AudioSeekFinishUC,
//    private val playerProgressRepo: PlayerProgressRepo,
//    private val scheduler: Scheduler
//) {
//    fun processIO(): ObservableTransformer<ProgressView.In, ProgressView.Out> =
//        scheduler.processIO(
//            inputMapper = inputMapper,
//            outputMapper = mapRepoToOutput,
//        )
//
//    private val inputMapper: (Observable<ProgressView.In>) -> Completable = { input ->
//        Completable.merge(listOf(
//            input.mapToUsecase<ProgressView.In.SeekToPosition> {
//                audioSeekProgressUC.execute(
//                    it.position
//                )
//            },
//            input.mapToUsecase<ProgressView.In.SeekingStarted> { audioSeekPauseUC.execute() },
//            input.mapToUsecase<ProgressView.In.SeekingFinished> { audioSeekFinishUC.execute() }
//        ))
//    }
//}