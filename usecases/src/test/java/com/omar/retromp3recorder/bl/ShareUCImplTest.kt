package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class ShareUCImplTest {

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        currentFileRepo.onNext(Optional("test"))
    }

    @Test
    fun `assert happy flow`() {
        val sharingModule = Mockito.mock(Sharer::class.java)
        whenever(sharingModule.share(any())).thenReturn(Completable.complete())
        val shareUC = ShareUC(sharingModule, currentFileRepo)

        //When
        shareUC.execute().test().assertNoErrors()

        //Then
        Mockito.verify(sharingModule).share(any())
    }
}