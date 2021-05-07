package com.omar.retromp3recorder.app.ui.visualizer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R

class VisualizerFragment : Fragment(R.layout.fragment_visualizer) {
    private val viewModel by viewModels<VisualizerViewModel>()
    private val visualizerDisplayView: VisualizerDisplayView?
        get() = view as? VisualizerDisplayView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recorder.observeRecorder().subscribe { bytes ->
            visualizerDisplayView?.updateVisualizer(bytes)
        }
    }
}