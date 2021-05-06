package com.omar.retromp3recorder.app.ui.files.rename

import android.app.Dialog
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.editorActionEvents
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe
import io.reactivex.rxjava3.disposables.Disposable

class RenameFileDialogFragment : DialogFragment() {
    private val viewModel by viewModels<RenameFileViewModel>()
    private lateinit var alertDialog: AlertDialog
    private lateinit var parent: View
    private val input: EditText
        get() = parent.findViewById(R.id.input)
    private var actionDisposable: Disposable? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parent = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_rename, null)
        alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rename_file))
            .setView(parent)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }
            .create()
        input.afterTextChangeEvents().subscribe {
            viewModel.input.onNext(RenameFileView.Input.CheckCanRename(newName = input.text.toString()))
        }
        viewModel.state.observe(this, ::render)
        return alertDialog
    }

    private fun render(state: RenameFileView.State) {
        if (state.shouldDismiss) dismiss()
        alertDialog.getButton(BUTTON_POSITIVE).isEnabled = state.isOkButtonEnabled

        actionDisposable?.dispose()
        actionDisposable = input.editorActionEvents().subscribe {
            if (state.isOkButtonEnabled) {
                viewModel.input.onNext(RenameFileView.Input.Rename(newName = input.text.toString()))
            } else {
                dismiss()
            }
        }
        alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener {
            viewModel.input.onNext(RenameFileView.Input.Rename(newName = input.text.toString()))
        }
        state.fileWrapper.ghost?.let { input.setText(it.path.toFileName()) }
    }
}
