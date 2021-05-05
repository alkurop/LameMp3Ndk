package com.omar.retromp3recorder.utils

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Observable<Shell<T>>.flatMapGhost(): Observable<T> = this.flatMap {
    val value = it.ghost
    if (value != null) Observable.just(value)
    else Observable.empty()
}

fun <In, Out> Scheduler.processIO(
    inputMapper: (Observable<In>) -> Completable,
    outputMapper: () -> Observable<Out>
): ObservableTransformer<In, Out> = ObservableTransformer { actions ->
    outputMapper().mergeWith(
        inputMapper(actions.observeOn(this)).subscribeOn(this)
    )
}

fun <T> Observable<T>.takeOne() = this.take(1)

inline fun <reified T> Observable<out Any>.mapToUsecase(crossinline action: (T) -> Completable): Completable =
    this.filter { it is T }.map { it as T }.flatMapCompletable { action(it) }