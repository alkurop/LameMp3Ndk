package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.DeleteFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.OpenFileButtonStateMapper
import com.omar.retromp3recorder.bl.TakeLastFileUC
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class CurrentFileInteractor @Inject constructor(
    private val takeLastFileUC: TakeLastFileUC,
    private val deleteFileButtonStateMapper: DeleteFileButtonStateMapper,
    private val openFileButtonStateMapper: OpenFileButtonStateMapper,
    private val currentFileRepo: CurrentFileRepo,
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
                            filePath.value
                        )
                    },
                deleteFileButtonStateMapper.observe()
                    .map { CurrentFileView.Output.DeleteButtonState(it) },
                openFileButtonStateMapper.observe()
                    .map { CurrentFileView.Output.OpenButtonState(it) },
            )
        )
    }
}