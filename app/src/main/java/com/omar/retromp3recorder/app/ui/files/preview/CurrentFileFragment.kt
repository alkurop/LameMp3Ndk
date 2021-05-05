package com.omar.retromp3recorder.app.ui.files.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.files.CurrentFileActivity
import com.omar.retromp3recorder.app.ui.files.delete.DeleteFileDialogFragment
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe

class CurrentFileFragment : Fragment(R.layout.fragment_current_file) {
    private val textView: TextView
        get() = findViewById(R.id.current_file_text)
    private val buttonOpen: View
        get() = findViewById(R.id.button_open)
    private val buttonDelete: View
        get() = findViewById(R.id.button_delete)
    private val viewModel: CurrentFileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
        buttonOpen.setOnClickListener {
            startActivity(Intent(requireContext(), CurrentFileActivity::class.java))
        }
        viewModel.input.onNext(CurrentFileView.Input.LookForPlayableFile)
    }

    private fun renderState(state: CurrentFileView.State) {
        textView.text = state.currentFile?.path?.toFileName() ?: getString(R.string.no_file)
        buttonOpen.setIsButtonActive(state.isOpenFileActive)
        buttonDelete.setIsButtonActive(state.isDeleteFileActive)
        if (state.currentFile != null)
            buttonDelete.setOnClickListener {
                DeleteFileDialogFragment().show(
                    childFragmentManager,
                    DeleteFileDialogFragment::class.java.canonicalName
                )
            }
    }
}

private fun View.setIsButtonActive(isVisible: Boolean) {
    this.isClickable = isVisible
    this.alpha = if (isVisible) 1.0f else 0.4f
}