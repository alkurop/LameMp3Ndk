package com.omar.retromp3recorder.bl.files

import com.google.common.truth.Truth.assertThat
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.CanRenameFileRepo
import com.omar.retromp3recorder.utils.Optional
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class CanRenameFileUCTest {
    @Inject
    lateinit var canRenameFileUC: CanRenameFileUC

    @Inject
    lateinit var canRenameFileRepo: CanRenameFileRepo
    private val file = "file".toTestFileWrapper()
    private val newName = "newName"

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `repo was updated with empty on start`() {
        val test = canRenameFileRepo.observe().test()
        canRenameFileUC.execute(file, newName).subscribe()
        val values = test.values()

        assertThat(values[1]).isEqualTo(Optional.empty<Boolean>())
    }

    @Test
    fun `repo was updated with result on end`() {
        val test = canRenameFileRepo.observe().test()
        canRenameFileUC.execute(file, newName).subscribe()
        val values = test.values()

        assertThat(values[2]).isEqualTo(Optional(true))
    }
}