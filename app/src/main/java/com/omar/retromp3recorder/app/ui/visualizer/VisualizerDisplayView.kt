package com.omar.retromp3recorder.app.ui.visualizer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import io.reactivex.rxjava3.subjects.BehaviorSubject

class VisualizerDisplayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPoints: FloatArray = FloatArray(0)
    private val mRect = Rect()
    private val mForePaint = Paint()
    private val bytesBus = BehaviorSubject.create<ByteArray>()

    init {
        mForePaint.strokeWidth = 1f
        mForePaint.isAntiAlias = true
        mForePaint.color = Color.rgb(0, 255, 0)
    }

    fun updateVisualizer(bytes: ByteArray) {
        bytesBus.onNext(bytes)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bytesBus.hasValue().not()) {
            return
        }
        val mBytes = bytesBus.blockingFirst()
        if (mPoints.size < mBytes.size * 4) {
            mPoints = FloatArray(mBytes.size * 4)
        }
        mRect[0, 0, width] = height
        val length = mBytes.size - 1
        val height = mRect.height()


        for (i in 0 until length) {
            val index = i * 4
            mPoints[index] = (mRect.width() * i / length).toFloat()
            mPoints[index + 2] = (mRect.width() * (i + 1) / length).toFloat()
            mPoints[index + 1] =
                height / 2 + ((mBytes[i] + Byte.MAX_VALUE).toByte() * (height / 2) / Byte.MAX_VALUE).toFloat()
            mPoints[index + 3] =
                height / 2 + ((mBytes[i + 1] + Byte.MAX_VALUE).toByte() * (height / 2) / Byte.MAX_VALUE).toFloat()
        }
        canvas.drawLines(mPoints, mForePaint)
    }
}

private const val i1 = Byte.MAX_VALUE
