package com.omar.retromp3recorder.app.ui.files.edit.delete

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.ui.utils.fileName

class DeleteFileDialogFragment : DialogFragment() {
    private val viewModel by viewModels<DeleteFileViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val filePath = requireArguments().getString(PARAM_FILEPATH)!!
        val fileName = filePath.fileName()
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete file")
            .setMessage(fileName)
            .setPositiveButton("Yes") { _, _ -> }
            .setNegativeButton("No") { _, _ -> }
            .create()
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