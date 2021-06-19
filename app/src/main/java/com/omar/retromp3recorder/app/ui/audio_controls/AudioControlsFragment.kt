package com.omar.retromp3recorder.app.ui.audio_controls

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.ui.state_button.InteractiveButtonBlinking
import com.omar.retromp3recorder.utils.toDisplay
import io.reactivex.rxjava3.core.Observable.merge

class AudioControlsFragment : Fragment(R.layout.fragment_audio_controls) {
    private val playButton: InteractiveButtonBlinking
        get() = findViewById(R.id.acf_play)
    private val recordButton: InteractiveButton
        get() = findViewById(R.id.acf_record)
    private val shareButton: InteractiveButton
        get() = findViewById(R.id.acf_share)
    private val stopButton: InteractiveButton
        get() = findViewById(R.id.acf_stop)
    private val progressView: TextView
        get() = findViewById(R.id.acf_player_progress)
    private val durationView: TextView
        get() = findViewById(R.id.acf_player_duration)
    private val viewModel: AudioControlsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        merge(
            listOf(
                playButton.clicks().map { AudioControlsView.Input.Play },
                recordButton.clicks().map { AudioControlsView.Input.Record },
                stopButton.clicks().map { AudioControlsView.Input.Stop },
                shareButton.clicks().map { AudioControlsView.Input.Share }
            ))
            .observe(viewLifecycleOwner) { viewModel.input.onNext(it) }
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: AudioControlsView.State) {
        playButton.onState(state.playButtonState)
        recordButton.onState(state.recordButtonState)
        shareButton.onState(state.shareButtonState)
        stopButton.onState(state.stopButtonState)
        state.playerProgressState.apply {
            progressView.isGone = this is PlayerProgressRepo.Out.Hidden
            durationView.isGone = this is PlayerProgressRepo.Out.Hidden
            this.takeIf { it is PlayerProgressRepo.Out.Shown }
                ?.run { this as PlayerProgressRepo.Out.Shown }
                ?.let {
                    progressView.text = it.progress.toDisplay()
                    durationView.text = it.duration.toDisplay()
                }
        }
    }
}