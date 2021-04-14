package com.omar.retromp3recorder.state.repos

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.di.DaggerRepoTestComponent
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class AudioStateRepoImplTest {
    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Mock
    lateinit var player: AudioPlayer

    @Mock
    lateinit var recorder: Mp3VoiceRecorder

    @Mock
    lateinit var fileEmptyChecker: FileEmptyChecker

    lateinit var audioStateRepo: AudioStateRepo

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        DaggerRepoTestComponent.create().inject(this)
        audioStateRepo = AudioStateRepoImpl(
            currentFileRepo = currentFileRepo,
            fileEmptyChecker = fileEmptyChecker,
            recorder = recorder,
            player = player
        )
        whenever(fileEmptyChecker.isFileEmpty(null)) doReturn true
        whenever(fileEmptyChecker.isFileEmpty(notNull())) doReturn false
    }

    @Test
    fun `when player is playing state playing`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Playing)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Idle)

        audioStateRepo.observe().test().assertNotComplete().assertNoErrors()
            .assertValue(AudioState.Playing)
    }

    @Test
    fun `when recorder is recording state recording`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Idle)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Recording)

        audioStateRepo.observe().test().assertNotComplete().assertNoErrors()
            .assertValue(AudioState.Recording)
    }

    @Test
    fun `when both idle and has file state Idle with file`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Idle)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Idle)
        currentFileRepo.onNext(Optional("test"))

        audioStateRepo.observe().test().assertNotComplete().assertNoErrors()
            .assertValue(AudioState.Idle(hasFile = true))
    }

    @Test
    fun `when both idle and no file state Idloe no file`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Idle)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Idle)

        audioStateRepo.observe().test().assertNotComplete().assertNoErrors()
            .assertValue(AudioState.Idle(hasFile = false))
    }
}