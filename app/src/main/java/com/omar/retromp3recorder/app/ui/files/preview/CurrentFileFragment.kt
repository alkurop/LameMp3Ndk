package com.omar.retromp3recorder.app.ui.files.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.files.CurrentFileActivity
import com.omar.retromp3recorder.app.ui.files.delete.DeleteFileDialogFragment
import com.omar.retromp3recorder.app.ui.files.rename.RenameFileDialogFragment
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.ui.wavetable.WavetableSeekbarPreview
import com.omar.retromp3recorder.utils.toSeekbarTime
import timber.log.Timber

class CurrentFileFragment : Fragment(R.layout.fragment_current_file) {
    private val textView: TextView
        get() = findViewById(R.id.current_file_text)
    private val buttonOpen: View
        get() = findViewById(R.id.button_open)
    private val buttonDelete: View
        get() = findViewById(R.id.button_delete)
    private val wavetablePreviewPreview: WavetableSeekbarPreview
        get() = findViewById(R.id.wavetable)
    private val viewModel: CurrentFileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
        buttonOpen.setOnClickListener {
            startActivity(Intent(requireContext(), CurrentFileActivity::class.java))
        }
        buttonDelete.setOnClickListener {
            DeleteFileDialogFragment().show(
                childFragmentManager,
                DeleteFileDialogFragment::class.java.canonicalName
            )
        }
        textView.setOnClickListener {
            RenameFileDialogFragment().show(
                childFragmentManager,
                RenameFileDialogFragment::class.java.canonicalName
            )
        }
        wavetablePreviewPreview.observeIsSeeking().observe(viewLifecycleOwner) {
            val event = when (it) {
                is WavetableSeekbarPreview.SeekState.SeekFinished ->
                    CurrentFileView.Input.SeekToPosition(it.progress)
                is WavetableSeekbarPreview.SeekState.SeekStarted -> {
                    CurrentFileView.Input.SeekingStarted
                }
            }
            viewModel.input.onNext(event)
        }
        wavetablePreviewPreview.observeIsSeeking().observe(viewLifecycleOwner) {
        }
    }

    private fun renderState(state: CurrentFileView.State) {
        textView.text = state.filePath?.toFileName() ?: getString(R.string.no_file)
        textView.isClickable = state.isRenameButtonActive
        buttonOpen.setIsButtonActive(state.isOpenFileActive)
        buttonDelete.setIsButtonActive(state.isDeleteFileActive)
        wavetablePreviewPreview.isInvisible = state.isRecording
        state.wavetable.ghost?.let {
            Timber.d("Progress wavetable")
            wavetablePreviewPreview.updateWavetable(it.data)
        }
        state.playerProgress.ghost?.let {
            Timber.d("Progress $it")
            wavetablePreviewPreview.updateProgress(it.run {
                Pair(
                    it.progress.toSeekbarTime(),
                    it.duration.toSeekbarTime()
                )
            })
        }
    }
}

private fun View.setIsButtonActive(isVisible: Boolean) {
    this.isClickable = isVisible
    this.alpha = if (isVisible) 1.0f else 0.4f
}