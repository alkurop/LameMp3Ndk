package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.state.CurrentFileRepo
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import javax.inject.Inject

class ShareUCImplTest {
    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `assert happy flow`() {
        val sharingModule = Mockito.mock(com.omar.retromp3recorder.share.Sharer::class.java)
        whenever(sharingModule.share(File("test"))).thenReturn(Completable.complete())
        val shareUC = ShareUC(sharingModule, currentFileRepo)

        //When
        shareUC.execute().subscribe()

        //Then
        Mockito.verify(sharingModule).share(any())
    }
}