package com.omar.retromp3recorder.app.ui.files.edit.delete

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.fileName
import com.omar.retromp3recorder.app.uiutils.observe

class DeleteFileDialogFragment : DialogFragment() {
    private val viewModel by viewModels<DeleteFileViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val filePath = requireArguments().getString(PARAM_FILEPATH)!!
        val fileName = filePath.fileName()
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_file))
            .setMessage(fileName)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.input.onNext(
                    DeleteFileView.Input.DeleteFile(
                        filePath
                    )
                )
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(this.viewLifecycleOwner, { dismiss() })
    }

    companion object {
        fun newInstance(filePath: String) =
            DeleteFileDialogFragment().apply {
                val bundle = Bundle().apply {
                    putString(PARAM_FILEPATH, filePath)
                }
                arguments = bundle
            }
    }
}

private const val PARAM_FILEPATH = "PARAM_FILEPATH"