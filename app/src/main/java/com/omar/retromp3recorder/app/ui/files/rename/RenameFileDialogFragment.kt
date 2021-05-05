package com.omar.retromp3recorder.app.ui.files.rename

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.uiutils.observe

class RenameFileDialogFragment : DialogFragment() {
    private val viewModel by viewModels<RenameFileViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_file))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
//                viewModel.input.onNext(RenameFileView.Input.DeleteFile(filePath = filePath))
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(this.viewLifecycleOwner, ::render)
    }

    private fun render(state: RenameFileView.State) {
    }
}
