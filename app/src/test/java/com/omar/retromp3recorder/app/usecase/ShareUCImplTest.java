package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.share.SharingModule;

import org.junit.Test;

import io.reactivex.Completable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShareUCImplTest {
    @Test
    public void test_UC_Executed(){
        SharingModule sharingModule = mock(SharingModule.class);
        when(sharingModule.share()).thenReturn(Completable.complete());
        ShareUCImpl shareUC = new ShareUCImpl(sharingModule);

        //When
        shareUC.execute().subscribe();

        //Then
        verify(sharingModule).share();
    }

}