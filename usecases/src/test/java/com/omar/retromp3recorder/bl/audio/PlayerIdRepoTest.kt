package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.stringerbell.Stringer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlayerIdRepoTest {
    @Mock
    lateinit var audioPlayer: AudioPlayer

    @InjectMocks
    lateinit var mapper: PlayerIdMapper
    private val playerSubject = PublishSubject.create<AudioPlayer.Event>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(audioPlayer.observeEvents()) doReturn playerSubject
    }

    @Test
    fun `repo listens to player event player Id`() {
        val consumer = mapper.observe().test()
        val playerId = 26

        playerSubject.onNext(AudioPlayer.Event.PlayerId(playerId))

        consumer.assertNoErrors().assertNotComplete().assertValue(playerId)
    }

    @Test
    fun `repo does not listen to message events`() {
        val consumer = mapper.observe().test()

        playerSubject.onNext(AudioPlayer.Event.Message(Stringer.ofString("hello")))

        consumer.assertNoErrors().assertNotComplete().assertNoValues()
    }

    @Test
    fun `player does not listen to error events`() {
        val consumer = mapper.observe().test()

        playerSubject.onNext(AudioPlayer.Event.Error(Stringer.ofString("sos")))

        consumer.assertNoErrors().assertNotComplete().assertNoValues()
    }
}