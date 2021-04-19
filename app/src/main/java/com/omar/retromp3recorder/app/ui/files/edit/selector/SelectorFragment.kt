package com.omar.retromp3recorder.app.ui.files.edit.selector

import androidx.fragment.app.Fragment
import com.omar.retromp3recorder.app.R

class SelectorFragment : Fragment(R.layout.fragment_selector) {
    interface Callback {
        fun onFileSelected(path: String)
    }
}