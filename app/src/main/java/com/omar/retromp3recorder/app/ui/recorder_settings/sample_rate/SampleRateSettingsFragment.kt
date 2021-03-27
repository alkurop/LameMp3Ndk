package com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding3.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.recorder_settings.RecorderSettingsBaseFragment
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.disposables.CompositeDisposable

class SampleRateSettingsFragment : RecorderSettingsBaseFragment() {
    private val compositeDisposable = CompositeDisposable()
    private val viewModel by viewModels<SampleRateViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTitleView(getString(R.string.sample_rate))
        val group = Mp3VoiceRecorder.SampleRate.values().map { sampleRate ->
            addCheckBox(
                title = getString(
                    R.string.sample_rate_format,
                    sampleRate.value
                )
            )
        }
        group.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .subscribe {
                    val sampleRate = Mp3VoiceRecorder.SampleRate.values()[index]
                    viewModel.inputSubject.onNext(sampleRate)
                }
        }.forEach { compositeDisposable.add(it) }

        viewModel.state.observe(viewLifecycleOwner, { state ->
            group.forEachIndexed { index, radioButton ->
                radioButton.isChecked = state.ordinal == index
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}