package com.omar.retromp3recorder.app.ui.visualizer

import android.media.audiofx.Visualizer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class VisualizerFragment : Fragment(R.layout.fragment_visualizer) {

    private val viewModel by viewModels<VisualizerViewModel>()
    private val visualizerDisplayView: VisualizerDisplayView
        get() = requireView() as VisualizerDisplayView

    private var visualizer: Visualizer? = null
    private val compositeDisposable = CompositeDisposable()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.audioState != AudioState.Playing) {
                stopVisualizer()
            }
            renderPlayerId(state.playerId.ghost)
        }
    }

    private fun stopVisualizer() {
        Completable
            .fromAction {
                if (visualizer != null) {
                    visualizer?.release()
                    visualizer = null
                }
            }
            .subscribeOn(Schedulers.computation())
            .subscribe()
            .disposedBy(compositeDisposable)
    }

    private fun renderPlayerId(playerId: Int?) {
        if (playerId == null) {
            return
        }
        visualizer = Visualizer(playerId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer,
                    bytes: ByteArray,
                    samplingRate: Int
                ) = visualizerDisplayView.updateVisualizer(bytes)

                override fun onFftDataCapture(
                    visualizer: Visualizer,
                    bytes: ByteArray,
                    samplingRate: Int
                ) = Unit
            }, Visualizer.getMaxCaptureRate() / 2, true, false)
            enabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopVisualizer()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}