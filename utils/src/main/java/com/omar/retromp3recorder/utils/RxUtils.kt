package com.omar.retromp3recorder.utils

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Observable<Shell<T>>.flatMapGhost(): Observable<T> = this.flatMap {
    val value = it.ghost
    if (value != null) Observable.just(value)
    else Observable.empty()
}

fun <In, Out> Scheduler.processIO(
    outMapper: (Observable<In>) -> Completable,
    inMappper: () -> Observable<Out>
): ObservableTransformer<In, Out> = ObservableTransformer { actions ->
    inMappper().mergeWith(
        outMapper(actions.observeOn(this))
    )
}