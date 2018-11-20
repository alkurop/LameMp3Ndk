package com.omar.retromp3recorder.app.files

import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

fun map() = ObservableTransformer<FilesResult, FilesViewModel> { upstream ->
    upstream.scan(getDefaultViewModel(), getMapper())
}

private fun getMapper() = BiFunction<FilesViewModel, FilesResult, FilesViewModel>{
    oldeModel, result ->
    when(result){
        is FilesResult.FileSelectedResult -> oldeModel.copy(
                selectedFile = result.fileName
        )
        is FilesResult.FilesListResult -> oldeModel.copy(
                files = result.files
        )
    }
}

private fun getDefaultViewModel(): FilesViewModel {
    return FilesViewModel(emptyList(), "")
}