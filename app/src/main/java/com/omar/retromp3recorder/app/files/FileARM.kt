package com.omar.retromp3recorder.app.files

sealed class FilesAction {
    data class SelectFileAction(val fileName: String) : FilesAction()
    data class DeleteFileAction(val filename: String) : FilesAction()
}

sealed class FilesResult {
    data class FileSelectedResult(val fileName: String) : FilesResult()
    data class FilesListResult(val files: List<String>) : FilesResult()
}

data class FilesViewModel(
        val files: List<String>,
        val selectedFile: String
)