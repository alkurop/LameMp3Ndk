package com.omar.retromp3recorder.state.repos

import com.github.alkurop.stringerbell.Stringer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LogRepoTest {
    @Mock
    lateinit var audioPlayer: AudioPlayer

    @Mock
    lateinit var mp3VoiceRecorder: Mp3VoiceRecorder

    @Mock
    lateinit var sharer: Sharer
    lateinit var logRepo: LogRepo
    private val message = Stringer.ofString("hello")
    private val audioPlayerSubject = PublishSubject.create<AudioPlayer.Event>()
    private val voiceRecorderSubject = PublishSubject.create<Mp3VoiceRecorder.Event>()
    private val sharerSubject = PublishSubject.create<Sharer.Event>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(audioPlayer.observeEvents()) doReturn audioPlayerSubject
        whenever(mp3VoiceRecorder.observeEvents()) doReturn voiceRecorderSubject
        whenever(sharer.observeEvents()) doReturn sharerSubject
        logRepo = LogRepo(
            audioPlayer = audioPlayer,
            recorder = mp3VoiceRecorder,
            sharer = sharer
        )
    }

    @Test
    fun `Mp3VoiceRecorder Event Message logs Message`() {
        val test = logRepo.observe().test()

        voiceRecorderSubject.onNext(Mp3VoiceRecorder.Event.Message(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Message(message))
    }

    @Test
    fun `Mp3VoiceRecorder Event Error logs Error`() {
        val test = logRepo.observe().test()

        voiceRecorderSubject.onNext(Mp3VoiceRecorder.Event.Error(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Error(message))
    }

    @Test
    fun `AudioPlayer Event Message logs Message`() {
        val test = logRepo.observe().test()

        audioPlayerSubject.onNext(AudioPlayer.Event.Message(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Message(message))
    }

    @Test
    fun `AudioPlayer Event Error logs Error`() {
        val test = logRepo.observe().test()

        audioPlayerSubject.onNext(AudioPlayer.Event.Error(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Error(message))
    }

    @Test
    fun `AudioPlayer id see no evil`() {
        val test = logRepo.observe().test()

        audioPlayerSubject.onNext(AudioPlayer.Event.PlayerId(17))

        test.assertNoErrors().assertNotComplete()
            .assertNoValues()
    }

    @Test
    fun `Sharer Event SharingOk logs Message`() {
        val test = logRepo.observe().test()

        sharerSubject.onNext(Sharer.Event.SharingOk(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Message(message))
    }

    @Test
    fun `Sharer Event SharingError logs Error`() {
        val test = logRepo.observe().test()

        sharerSubject.onNext(Sharer.Event.Error(message))

        test.assertNoErrors().assertNotComplete()
            .assertValue(LogRepo.Event.Error(message))
    }
}