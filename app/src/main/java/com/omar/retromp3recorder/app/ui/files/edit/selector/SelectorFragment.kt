package com.omar.retromp3recorder.app.ui.files.edit.selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.uiutils.observe

class SelectorFragment : Fragment(R.layout.fragment_selector) {
    private val viewModel by viewModels<SelectorViewModel>()
    private val recyclerView: RecyclerView
        get() = findViewById(R.id.recycler_view)
    private val adapter = SelectorAdapter {
        viewModel.inputSubject.onNext(SelectorView.Input.ItemSelected(it))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: SelectorView.State) {
        adapter.items = state.items
    }
}