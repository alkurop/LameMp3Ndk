package com.omar.retromp3recorder.app.share

import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.di.AppComponent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoggingSharingModule @Inject internal constructor(
    @param:Named(AppComponent.DECORATOR_A) private val sharingModule: SharingModule,
    logRepo: LogRepo,
    scheduler: Scheduler
) : SharingModule {

    override fun share(): Completable {
        return sharingModule.share()
    }

    override fun observeEvents(): Observable<SharingModule.Event> {
        return sharingModule.observeEvents()
    }

    init {
        val share = sharingModule.observeEvents().share()
        val message = share.ofType(SharingModule.Event.SharingOk::class.java)
            .map { answer -> LogRepo.Event.Message(answer.message) }
        val error = share.ofType(SharingModule.Event.SharingError::class.java)
            .map { answer -> LogRepo.Event.Error(answer.error) }
        Observable.merge(listOf(message, error))
            .flatMapCompletable { event  ->
                Completable.fromAction { logRepo.newValue(event) }
            }
            .subscribeOn(scheduler)
            .subscribe()
    }
}