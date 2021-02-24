package com.omar.retromp3recorder.app.common.usecase

import io.reactivex.Completable

interface NoParamsUseCase {
    fun execute(): Completable
}