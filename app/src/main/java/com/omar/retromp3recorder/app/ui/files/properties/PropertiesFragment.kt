package com.omar.retromp3recorder.app.ui.files.properties

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.files.rename.RenameFileDialogFragment
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe

class PropertiesFragment : Fragment(R.layout.fragment_properties) {
    private val viewModel by viewModels<PropertiesViewModel>()
    private val titleView: TextView
        get() = findViewById(R.id.title)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: PropertiesView.State) {
        val currentFile = state.currentFile
        requireView().isVisible = currentFile != null
        if (currentFile != null) {
            titleView.text = currentFile.path.toFileName()
            findViewById<View>(R.id.edit_title).clicks()
                .subscribe {
                    RenameFileDialogFragment()
                        .show(
                            childFragmentManager,
                            RenameFileDialogFragment::class.java.canonicalName
                        )
                }
        }
    }
}