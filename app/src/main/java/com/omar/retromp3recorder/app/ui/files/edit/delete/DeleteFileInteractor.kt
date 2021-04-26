package com.omar.retromp3recorder.app.ui.files.edit.delete

import com.omar.retromp3recorder.bl.DeleteFileUC
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class DeleteFileInteractor @Inject constructor(
    private val deleteFileUC: DeleteFileUC,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<DeleteFileView.Input, DeleteFileView.Output> =
        ObservableTransformer { upstream ->
            upstream.observeOn(scheduler)
                .compose { input ->
                    input
                        .ofType(DeleteFileView.Input.DeleteFile::class.java)
                        .flatMap {
                            deleteFileUC.execute(it.filePath)
                                .andThen(Observable.just(DeleteFileView.Output.Finished))
                        }
                }
        }
}