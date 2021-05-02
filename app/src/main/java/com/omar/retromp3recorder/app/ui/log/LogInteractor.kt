package com.omar.retromp3recorder.app.ui.log

import com.omar.retromp3recorder.state.repos.LogRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class LogInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val logRepo: LogRepo
) {
    fun processIO(): ObservableTransformer<LogView.Input, LogView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<LogView.Output> = {
        Observable.merge(
            listOf(
                logRepo.observe()
                    .ofType(LogRepo.Event.Message::class.java)
                    .map { message -> LogView.Output.MessageLogOutput(message.message) },
                logRepo.observe()
                    .ofType(LogRepo.Event.Error::class.java)
                    .map { message -> LogView.Output.ErrorLogOutput(message.error) },
            )
        )
    }
}