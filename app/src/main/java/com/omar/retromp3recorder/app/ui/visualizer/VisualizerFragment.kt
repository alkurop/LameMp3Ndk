package com.omar.retromp3recorder.app.ui.visualizer

import android.media.audiofx.Visualizer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.audio.AudioState

class VisualizerFragment : Fragment(R.layout.fragment_visualizer) {
    private val viewModel by viewModels<VisualizerViewModel>()
    private val visualizerDisplayView: VisualizerDisplayView?
        get() = view as? VisualizerDisplayView
    private var visualizer: Visualizer? = null
    private val visualizerListener = object : Visualizer.OnDataCaptureListener {
        override fun onWaveFormDataCapture(
            visualizer: Visualizer,
            bytes: ByteArray,
            samplingRate: Int
        ) {
            visualizerDisplayView?.updateVisualizer(bytes)
        }

        override fun onFftDataCapture(
            visualizer: Visualizer,
            bytes: ByteArray,
            samplingRate: Int
        ) = Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.audioState != AudioState.Playing) {
                stopVisualizer()
            } else {
                renderPlayerId(state.playerId)
            }
        }
        viewModel.recorderImpl.observeRecorder().subscribe { bytes ->
            visualizerDisplayView?.updateVisualizer(bytes)
        }
    }

    private fun stopVisualizer() {
        visualizer?.enabled = false
        visualizer?.setDataCaptureListener(null, Visualizer.getMaxCaptureRate() / 2, true, false)
        visualizer?.release()
        visualizer = null
    }

    private fun renderPlayerId(playerId: Int?) {
        if (playerId == null || visualizer?.enabled == true) {
            return
        }
        visualizer = Visualizer(playerId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]

            setDataCaptureListener(
                visualizerListener,
                Visualizer.getMaxCaptureRate(),
                true,
                false
            )
            enabled = true
        }
    }

    override fun onDestroyView() {
        stopVisualizer()
        super.onDestroyView()
    }
}