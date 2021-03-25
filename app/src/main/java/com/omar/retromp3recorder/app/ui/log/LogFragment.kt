package com.omar.retromp3recorder.app.ui.log

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById

class LogFragment : Fragment(R.layout.fragment_log) {

    private val scrollView: ScrollView
        get() = requireView() as ScrollView

    private val container: LinearLayout
        get() = findViewById(R.id.log_holder)

    private val viewModel by viewModels<LogViewModel>()
    private val scrollDownHandler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.message.ghost?.let { message -> renderMessage(message.bell(requireContext())) }
            state.error.ghost?.let { message -> renderError(message.bell(requireContext())) }
        }
    }

    private fun renderMessage(message: String) {
        @SuppressLint("InflateParams") val inflate = layoutInflater.inflate(
            R.layout.log_item_view,
            null
        ) as TextView
        container.addView(inflate)
        inflate.text = message
        scrollDownHandler.postDelayed(
            { scrollView.fullScroll(View.FOCUS_DOWN) },
            DELAY_MILLIS.toLong()
        )
    }

    private fun renderError(error: String) {
        @SuppressLint("InflateParams") val inflate =
            layoutInflater.inflate(R.layout.log_item_view, null) as TextView
        container.addView(inflate)
        inflate.text = getString(
            R.string.error_string,
            error
        )
        inflate.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_orange_light
            )
        )
        scrollDownHandler.postDelayed(
            { scrollView.fullScroll(View.FOCUS_DOWN) },
            DELAY_MILLIS.toLong()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scrollDownHandler.removeCallbacksAndMessages(null)
    }
}

private const val DELAY_MILLIS = 150