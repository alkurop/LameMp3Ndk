package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import java.io.File

interface FileRenamer {
    fun renameFile(fileWrapper: ExistingFileWrapper, newName: String): String

    fun canRename(fileWrapper: ExistingFileWrapper, newName: String): Boolean
}

class FileRenameImpl : FileRenamer {
    override fun renameFile(fileWrapper: ExistingFileWrapper, newName: String): String {
        val newFile = generateNewFile(fileWrapper, newName)
        File(fileWrapper.path).renameTo(newFile)
        return newFile.path
    }

    override fun canRename(fileWrapper: ExistingFileWrapper, newName: String): Boolean {
        val generateNewFile = generateNewFile(fileWrapper, newName)
        return generateNewFile.exists().not()
    }

    private fun generateNewFile(fileWrapper: ExistingFileWrapper, newName: String): File {
        val split = fileWrapper.path.split("/")
        val pathWithoutName = split.dropLast(1).joinToString(separator = "/")
        val oldFileName = split.last()
        val ext = oldFileName.split(".").last()
        val newFileName = "$newName.$ext"
        return File("$pathWithoutName/$newFileName")
    }
}