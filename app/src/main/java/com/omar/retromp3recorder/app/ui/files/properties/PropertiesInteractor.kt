package com.omar.retromp3recorder.app.ui.files.properties

import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class PropertiesInteractor @Inject constructor(
    private val filePropertiesMapper: FilePropertiesMapper,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<PropertiesView.Input, PropertiesView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<PropertiesView.Output> = {
        Observable.merge(
            listOf(
                filePropertiesMapper.observe()
            )
        )
    }
}