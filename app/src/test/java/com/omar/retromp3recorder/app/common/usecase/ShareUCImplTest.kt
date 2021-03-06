package com.omar.retromp3recorder.app.common.usecase

import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.share.ShareUC
import com.omar.retromp3recorder.app.share.SharingModule
import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito

class ShareUCImplTest {
    @Test
    fun test_UC_Executed() {
        val sharingModule = Mockito.mock(SharingModule::class.java)
        whenever(sharingModule.share()).thenReturn(Completable.complete())
        val shareUC = ShareUC(sharingModule)

        //When
        shareUC.execute().subscribe()

        //Then
        Mockito.verify(sharingModule).share()
    }
}