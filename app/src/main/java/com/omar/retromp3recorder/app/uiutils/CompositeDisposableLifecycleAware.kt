package com.omar.retromp3recorder.app.uiutils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun compositeDisposableLifecycleAware(initialise: () -> LifecycleOwner): ReadOnlyProperty<Any, CompositeDisposable> =
    object : ReadOnlyProperty<Any, CompositeDisposable>, LifecycleObserver {
        private val compositeDisposable = CompositeDisposable()
        private var lifecycle: Lifecycle? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            compositeDisposable.clear()
            lifecycle?.removeObserver(this)
        }

        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ): CompositeDisposable {
            lifecycle = initialise().lifecycle.also {
                it.addObserver(this)
            }
            return compositeDisposable
        }
    }

fun <T> Observable<T>.subscribe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
) {
    val observable = this.observeOn(AndroidSchedulers.mainThread())
    val disposable =
        if (onError != null) observable.subscribe(onNext, onError)
        else observable.subscribe(onNext)
    val lifecycle = lifecycleOwner.lifecycle
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            disposable.dispose()
            lifecycle.removeObserver(this)
        }
    }
    lifecycle.addObserver(observer)
}

fun <T> Observable<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)
) = this.subscribe(lifecycleOwner, onNext, onError)

fun <T> Observable<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit
) = this.subscribe(lifecycleOwner, onNext)