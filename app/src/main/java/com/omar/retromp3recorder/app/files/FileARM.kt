package com.omar.retromp3recorder.app.files

sealed class FilesAction {
    data class SelectFileAction(val fileName: String) : FilesAction()
    data class DeleteFileAction(val filename: String) : FilesAction()
}

sealed class FilesResult {
    data class FileSelectedTesult(val fileName: String) : FilesResult()
}

data class FilesModel(val files: List<String>)