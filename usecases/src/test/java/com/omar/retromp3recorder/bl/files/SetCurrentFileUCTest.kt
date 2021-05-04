package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SetCurrentFileUCTest {
    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var setCurrentFileUC: SetCurrentFileUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        currentFileRepo.onNext(Optional("test"))
    }

    @Test
    fun `filepath was set to CurrentFileRepo`() {
        val filePath = "Lesha"
        setCurrentFileUC.execute(filePath).test().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional(filePath)).assertNoErrors()
            .assertNotComplete()
    }
}