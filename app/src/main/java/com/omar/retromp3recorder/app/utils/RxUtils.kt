package com.omar.retromp3recorder.app.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable){
    compositeDisposable.add(this)
}