package com.omar.retromp3recorder.ui.wavetable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class WavetableSeekbarPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val wavetableSeekbar: WavetableSeekbar
        get() = findViewById(R.id.seek_bar)
    private val wavetablePreview: WavetablePreview
        get() = findViewById(R.id.wavetable_preview)

    init {
        View.inflate(context, R.layout.view_wavetable_seekbar, this)
    }

    fun updateWavetable(bytes: ByteArray) {
        wavetablePreview.update(bytes)
    }

    fun updateProgress(progress: Pair<Long, Long>) {
        wavetableSeekbar.update(progress)
    }
}