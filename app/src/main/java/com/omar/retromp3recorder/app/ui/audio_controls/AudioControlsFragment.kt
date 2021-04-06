package com.omar.retromp3recorder.app.ui.audio_controls

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding3.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import io.reactivex.Observable.merge

class AudioControlsFragment : Fragment(R.layout.fragment_audio_controls) {
    private val playButton: InteractiveButton
        get() = findViewById(R.id.acf_play)

    private val recordButton: InteractiveButton
        get() = findViewById(R.id.acf_record)

    private val shareButton: InteractiveButton
        get() = findViewById(R.id.acf_share)

    private val stopButton: InteractiveButton
        get() = findViewById(R.id.acf_stop)

    private val viewModel: AudioControlsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        merge(listOf(
            recordButton.clicks().map { AudioControlsView.Input.Record },
            playButton.clicks().map { AudioControlsView.Input.Play },
            stopButton.clicks().map { AudioControlsView.Input.Stop },
            shareButton.clicks().map { AudioControlsView.Input.Share }
        )).observe(viewLifecycleOwner) { viewModel.onInput(it) }
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: AudioControlsView.State) {
        when (val audioState = state.audioState) {
            is AudioState.Idle -> {
                playButton.onState(if (audioState.hasFile) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED)
                shareButton.onState(if (audioState.hasFile) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED)
                recordButton.onState(InteractiveButton.State.ENABLED)
                stopButton.onState(InteractiveButton.State.DISABLED)
            }
            AudioState.Playing -> {
                shareButton.onState(InteractiveButton.State.ENABLED)
                playButton.onState(InteractiveButton.State.DISABLED)
                recordButton.onState(InteractiveButton.State.DISABLED)
                stopButton.onState(InteractiveButton.State.ENABLED)
            }
            AudioState.Recording -> {
                shareButton.onState(InteractiveButton.State.DISABLED)
                playButton.onState(InteractiveButton.State.DISABLED)
                recordButton.onState(InteractiveButton.State.DISABLED)
                stopButton.onState(InteractiveButton.State.ENABLED)
            }
        }
    }
}