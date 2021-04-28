package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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

    companion object {
        const val RECORDER_SUBJECT = "RECORDER_SUBJECT"
        const val PLAYER_SUBJECT = "PLAYER_SUBJECT"
        const val SHARING_SUBJECT = "SHARING_SUBJECT"
    }
}