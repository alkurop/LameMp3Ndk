package com.omar.retromp3recorder.ui.wavetable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class WavetableSeekbarPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val wavetableProgressBar: WavetableProgressBar
        get() = findViewById(R.id.progress_bar)
    private val wavetablePreview: WavetablePreview
        get() = findViewById(R.id.wavetable_preview)
    private val seekbar: SeekBar
        get() = findViewById(R.id.seek_bar)
    private var shouldUpdateProgressBar = true
    private var progressUpdate = PublishSubject.create<Pair<Int, Int>>()

    fun observeProgress(): Observable<Pair<Int, Int>> = progressUpdate

    init {
        View.inflate(context, R.layout.view_wavetable_seekbar, this)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var userChangedProgress: Pair<Int, Int> = Pair(0, 0)
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    userChangedProgress = Pair(progress, (seekbar.max))
                    wavetableProgressBar.update(userChangedProgress)
                }
            }

            override fun onStartTrackingTouch(v: SeekBar?) {
                shouldUpdateProgressBar = false
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                progressUpdate.onNext(userChangedProgress)
                shouldUpdateProgressBar = true
            }
        })
        seekbar.setPadding(0, 0, 0, 0);
    }

    fun updateWavetable(bytes: ByteArray) {
        wavetablePreview.update(bytes)
        val update = Pair(0, bytes.size)
        updateProgress(update)
    }

    fun updateProgress(progress: Pair<Int, Int>) {
        if (shouldUpdateProgressBar) {
            wavetableProgressBar.update(progress)
            seekbar.max = (progress.second)
            seekbar.progress = (progress.first)
        }
    }
}