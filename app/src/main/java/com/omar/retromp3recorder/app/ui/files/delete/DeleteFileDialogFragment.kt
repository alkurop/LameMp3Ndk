package com.omar.retromp3recorder.app.ui.files.delete

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe

class DeleteFileDialogFragment : DialogFragment() {
    private val viewModel by viewModels<DeleteFileViewModel>()
    private lateinit var dialog: AlertDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_file))
            .setMessage("fileName")
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }.create()
        viewModel.state.observe(this, ::render)
        return dialog
    }

    private fun render(state: DeleteFileView.State) {
        state.fileWrapper?.let {
            dialog.setMessage(it.path.toFileName())
        }
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            viewModel.input.onNext(DeleteFileView.Input.DeleteFile)
        }
        state.shouldDismiss.takeIf { it }?.let { dismiss() }
    }
}
