package com.omar.retromp3recorder.app.common.usecase

import io.reactivex.Completable

interface UseCase<T> {
    fun execute(params: T): Completable
}
