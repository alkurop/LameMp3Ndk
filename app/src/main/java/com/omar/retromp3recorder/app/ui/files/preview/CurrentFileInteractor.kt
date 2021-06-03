package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.DeleteFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.OpenFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.RenameFileButtonStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.files.CurrentFileMapper
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
    private val renameFileButtonStateMapper: RenameFileButtonStateMapper,
    private val playerProgressMapper: PlayerProgressMapper,
    private val scheduler: Scheduler,
) {
    fun processIO(): ObservableTransformer<CurrentFileView.Input, CurrentFileView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<CurrentFileView.Output> = {
        Observable.merge(
            listOf(
                playerProgressMapper.observe().map {
                    CurrentFileView.Output.PlayerProgress(it)
                },
                renameFileButtonStateMapper.observe().map {
                    CurrentFileView.Output.RenameButtonState(it)
                },
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