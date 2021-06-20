package com.omar.retromp3recorder.app.ui.joined_progress
//class JoinedProgressMapper @Inject constructor(
//    private val playerProgressRepo: PlayerProgressRepo,
//    private val mp3VoiceRecorder: Mp3VoiceRecorder
//) {
//    //whenever a recording is started, a new file gets created.
//    //then player progress repo sets to hidden, cuz not a playable file yet
//    fun observe() {
//        Observable.combineLatest(
//            playerProgressRepo.observe(),
//            mp3VoiceRecorder.observeRecorder(), { }
//        )
//    }
//}
//
//sealed class JoinedProgress {
//    object Hidden : JoinedProgress()
//    object RecorderProgress : JoinedProgress()
//    data class PlayerProgress(
//        val progress: PlayerProgressRepo.Out.Shown
//    ) : JoinedProgress()
//}
//
//data class RecorderProgress(
//    val progress: Long,
//    val wavetable: Wavetable
//)