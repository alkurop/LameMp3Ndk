package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.FileWrapper
import java.io.File

interface FileRenamer {
    fun renameFile(fileWrapper: FileWrapper, newName: String)

    fun canRename(fileWrapper: FileWrapper, newName: String): Boolean
}

class FileRenameImpl : FileRenamer {
    override fun renameFile(fileWrapper: FileWrapper, newName: String) {
        File(fileWrapper.path).renameTo(generateNewFile(fileWrapper, newName))
    }

    override fun canRename(fileWrapper: FileWrapper, newName: String): Boolean {
        return generateNewFile(fileWrapper, newName).exists().not()
    }

    private fun generateNewFile(fileWrapper: FileWrapper, newName: String): File {
        val split = fileWrapper.path.split("/")
        val pathWithoutName = split.dropLast(1).joinToString(separator = "/")
        val oldFileName = split.last()
        val ext = oldFileName.split(".").last()
        val newFileName = "$newName.$ext"
        return File(pathWithoutName + newFileName)
    }
}