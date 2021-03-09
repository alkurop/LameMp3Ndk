package com.omar.retromp3recorder.app.playback.player

import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class StateLoggingAudioPlayer @Inject internal constructor(
    private val stateRepo: StateRepo,
    @param:Named(AppComponent.DECORATOR_B) private val audioPlayer: AudioPlayer,
    playerIdRepo: PlayerIdRepo

) : AudioPlayer {

    private val observable: Observable<AudioPlayer.Event>
    override val isPlaying: Boolean
        get() = audioPlayer.isPlaying

    init {
        val stateCompletable = Observable
            .merge(listOf(
                audioPlayer.observeEvents()
                    .ofType(Error::class.java)
                    .map { MainView.State.Idle },
                audioPlayer.observeEvents()
                    .ofType(AudioPlayer.Event.PlaybackEnded::class.java)
                    .map { MainView.State.Idle }
            ))
            .switchMapCompletable { state ->
                Completable.fromAction {
                    stateRepo.newValue(state)
                }
            }

        val playerIdCompletable: Completable = audioPlayer
            .observeEvents()
            .ofType(AudioPlayer.Event.SendPlayerId::class.java)
            .switchMapCompletable { sendPlayerId ->
                Completable.fromAction { playerIdRepo.newValue(sendPlayerId.playerId) }
            }

        observable = audioPlayer
            .observeEvents()
            .mergeWith(
                Completable.merge(listOf(stateCompletable, playerIdCompletable))
            )
            .share()
    }

    override fun playerStop() {
        if (audioPlayer.isPlaying) {
            stateRepo.newValue(MainView.State.Idle)
            audioPlayer.playerStop()
        }
    }

    override fun playerStart(voiceURL: String) {
        stateRepo.newValue(MainView.State.Playing)
        audioPlayer.playerStart(voiceURL)
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return observable
    }
}
