package com.omar.retromp3recorder.app.usecases

import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.modules.share.Sharer
import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito

class ShareUCImplTest {
    @Test
    fun `assert happy flow`() {
        val sharingModule = Mockito.mock(Sharer::class.java)
        whenever(sharingModule.share()).thenReturn(Completable.complete())
        val shareUC = ShareUC(sharingModule)

        //When
        shareUC.execute().subscribe()

        //Then
        Mockito.verify(sharingModule).share()
    }
}