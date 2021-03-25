package com.omar.retromp3recorder.app.ui.log

import com.omar.retromp3recorder.state.repos.LogRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
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