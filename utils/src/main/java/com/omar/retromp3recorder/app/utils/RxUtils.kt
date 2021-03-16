package com.omar.retromp3recorder.app.utils

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.Observable
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