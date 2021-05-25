package com.omar.retromp3recorder.app.ui.files.preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import io.reactivex.rxjava3.subjects.BehaviorSubject

class WavetablePreview @JvmOverloads constructor(
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

    fun update(bytes: ByteArray) {
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
        if (mPoints.size != mBytes.size * 8) {
            mPoints = FloatArray(mBytes.size * 8)
        }
        mRect[0, 0, width] = height
        val length = mBytes.size - 1
        val height = mRect.height()


        for (i in 0 until length) {
            val index = i * 8
            val m = Byte.MAX_VALUE
            mPoints[index] = (mRect.width() * i / length).toFloat()
            mPoints[index + 1] = height / 2 + ((mBytes[i]) * (height / 2) / m).toFloat()
            mPoints[index + 2] = (mRect.width() * (i + 1) / length).toFloat()
            mPoints[index + 3] = height / 2 - (mBytes[i] * (height / 2) / m).toFloat()

            mPoints[4 + index] = (mRect.width() * i / length).toFloat()
            mPoints[4 + index + 1] = height / 2 + ((mBytes[i]) * (height / 2) / m).toFloat()
            mPoints[4 + index + 2] = (mRect.width() * (i + 1) / length).toFloat()
            mPoints[4 + index + 3] = height / 2 - (mBytes[i] * (height / 2) / m).toFloat()
        }
        canvas.drawLines(mPoints, mForePaint)
    }
}