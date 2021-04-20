package com.omar.retromp3recorder.app.ui.files.edit.selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById

class SelectorFragment : Fragment(R.layout.fragment_selector) {
    private val recyclerView: RecyclerView
        get() = findViewById(R.id.recycler_view)
    private val adapter = SelectorAdapter {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}