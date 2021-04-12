package com.omar.retromp3recorder.app.ui.files.preview

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.uiutils.observe

class CurrentFileFragment : Fragment(R.layout.fragment_current_file) {
    private val textView: TextView
        get() = findViewById(R.id.current_file_text)

    private val viewModel: CurrentFileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: CurrentFileView.State) {
        textView.text = state.currentFileName.bell(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.input.onNext(CurrentFileView.Input.LookForPlayableFile)
    }
}