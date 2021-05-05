package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.DeleteFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.OpenFileButtonStateMapper
import com.omar.retromp3recorder.bl.files.CurrentFileMapper
import com.omar.retromp3recorder.bl.files.TakeLastFileUC
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class CurrentFileInteractor @Inject constructor(
    private val currentFileMapper: CurrentFileMapper,
    private val deleteFileButtonStateMapper: DeleteFileButtonStateMapper,
    private val openFileButtonStateMapper: OpenFileButtonStateMapper,
    private val scheduler: Scheduler,
    private val takeLastFileUC: TakeLastFileUC
) {
    fun processIO(): ObservableTransformer<CurrentFileView.Input, CurrentFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUC,
            outputMapper = mapRepoToOutput
        )

    private val mapInputToUC: (Observable<CurrentFileView.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.mapToUsecase<CurrentFileView.Input.LookForPlayableFile> { takeLastFileUC.execute() },
            )
        )
    }
    private val mapRepoToOutput: () -> Observable<CurrentFileView.Output> = {
        Observable.merge(
            listOf(
                currentFileMapper.observe().map { file ->
                    CurrentFileView.Output.CurrentFileOutput(
                        file.value
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