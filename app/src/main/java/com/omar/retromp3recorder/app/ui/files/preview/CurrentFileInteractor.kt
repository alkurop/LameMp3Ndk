package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.bl.TakeLastFileUC
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.filepathToFileName
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class CurrentFileInteractor @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val takeLastFileUC: TakeLastFileUC,
    private val audioStateRepo: AudioStateRepo,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<CurrentFileView.Input, CurrentFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUC,
            outputMapper = mapRepoToOutput
        )

    private val mapInputToUC: (Observable<CurrentFileView.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.ofType(CurrentFileView.Input.LookForPlayableFile::class.java)
                    .flatMapCompletable { takeLastFileUC.execute() },
            )
        )
    }
    private val mapRepoToOutput: () -> Observable<CurrentFileView.Output> = {
        Observable.merge(
            listOf(
                currentFileRepo.observe()
                    .map { filePath ->
                        CurrentFileView.Output.CurrentFileOutput(
                            filePath.value?.filepathToFileName()
                        )
                    },
                audioStateRepo.observe().ofType(AudioState.Idle::class.java)
                    .map { CurrentFileView.Output.AudioInactive },
                audioStateRepo.observe().ofType(AudioState.Recording::class.java)
                    .map { CurrentFileView.Output.AudioActive },
                audioStateRepo.observe().ofType(AudioState.Playing::class.java)
                    .map { CurrentFileView.Output.AudioActive }
            )
        )
    }
}