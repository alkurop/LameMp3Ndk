package com.omar.retromp3recorder.app.ui.audio_controls

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById

class AudioControlsFragment : Fragment(R.layout.fragment_audio_controls) {
    private val playButton: ImageView
        get() = findViewById(R.id.acf_play)

    private val recordButton: ImageView
        get() = findViewById(R.id.acf_record)

    private val shareButton: ImageView
        get() = findViewById(R.id.acf_share)

    private val viewModel: AudioControlsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: AudioControlsView.State) {

    }
}