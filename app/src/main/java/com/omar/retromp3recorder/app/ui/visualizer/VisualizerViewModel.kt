package com.omar.retromp3recorder.app.ui.visualizer

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderImpl
import javax.inject.Inject

class VisualizerViewModel : ViewModel() {
    @Inject
    lateinit var recorder: Mp3VoiceRecorderImpl

    init {
        App.appComponent.inject(this)
    }
}