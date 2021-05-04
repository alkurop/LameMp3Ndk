package com.omar.retromp3recorder.bl.files

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.verify
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FileRenamer
import com.omar.retromp3recorder.utils.Optional
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class RenameFileUCTest {
    @Inject
    lateinit var fileRenamer: FileRenamer

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var renameFileUC: RenameFileUC

    @Inject
    lateinit var fileLister: FileLister

    @Inject
    lateinit var dao: FileDbEntityDao
    private val file = "file".toTestFileWrapper()
    private val newName = "newName"

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `file renamed`() {
        renameFileUC.execute(file, newName).test().assertNoErrors().assertComplete()

        verify(fileRenamer).renameFile(file, newName)
    }

    @Test
    fun `new path was generated`() {
        renameFileUC.execute(file, newName).subscribe()

        currentFileRepo.observe().test().assertValue(Optional(newName))
    }

    @Test
    fun `database was updated`() {
        renameFileUC.execute(file, newName).subscribe()


        verify(dao).updateItem(check {
            assertThat(it.filepath).isEqualTo(
                newName
            )
        })
    }

    @Test
    fun `currentFileRepo was updated`() {
        renameFileUC.execute(file, newName).subscribe()

        currentFileRepo.observe().test().assertValue(Optional(newName))
    }

    @Test
    fun `look for files uc was executed`() {
        renameFileUC.execute(file, newName).subscribe()

        verify(fileLister).listFiles(any())
    }
}