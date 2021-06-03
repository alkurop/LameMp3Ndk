package com.omar.retromp3recorder.ui.wavetable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.subjects.BehaviorSubject

class WavetableSeekbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint().apply {
        strokeWidth = 0f
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
    }
    private val bytesBus = BehaviorSubject.create<Pair<Long, Long>>()

    fun update(update: Pair<Long, Long>) {
        bytesBus.onNext(update)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bytesBus.hasValue().not()) {
            return
        }
        val (progress, length) = bytesBus.blockingFirst()
        if (length == 0L) return
        val path = Path()
        val start = 0
        val end = width * progress / length
        val rect = RectF(
            start.toFloat(),
            1f,
            end.toFloat(),
            height.toFloat(),
        )
        path.addRect(rect, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }
}