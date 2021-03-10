package com.omar.retromp3recorder.app.di

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC
import com.omar.retromp3recorder.app.share.Sharer
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.mockito.Mockito
import javax.inject.Named
import javax.inject.Singleton

@Module
class MockModule {
    @Singleton
    @Provides
    @Named(RECORDER_SUBJECT)
    fun provideRecorderSubject(): Subject<Mp3VoiceRecorder.Event> {
        return PublishSubject.create()
    }

    @Singleton
    @Provides
    @Named(PLAYER_SUBJECT)
    fun providePlayerSubject(): Subject<AudioPlayer.Event> {
        return PublishSubject.create()
    }

    @Singleton
    @Provides
    @Named(SHARING_SUBJECT)
    fun provideSharingSubject(): Subject<Sharer.Event> {
        return BehaviorSubject.create()
    }

    @Singleton
    @Provides
    fun provideCheckPermissionsUC(): CheckPermissionsUC {
        val mock = Mockito.mock(CheckPermissionsUC::class.java)
        whenever(mock.execute(any())).thenReturn(Completable.complete())
        return mock
    }

    companion object {
        const val RECORDER_SUBJECT = "RECORDER_SUBJECT"
        const val PLAYER_SUBJECT = "PLAYER_SUBJECT"
        const val SHARING_SUBJECT = "SHARING_SUBJECT"
    }
}