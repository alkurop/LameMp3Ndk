package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ExistingFileMapper @Inject constructor(
    private val currentFileMapper: CurrentFileMapper,
) {
    fun observe(): Observable<Optional<ExistingFileWrapper>> {
        return currentFileMapper
            .observe()
            .map { option ->
                Optional(option.value as? ExistingFileWrapper)
            }
    }
}