package com.omar.retromp3recorder.ui.state_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout


class InteractiveButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
        get() = findViewById(R.id.vsb_image_view)

    init {
        View.inflate(context, R.layout.view_state_button, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InteractiveButton, 0, 0)
        try {
            typedArray.getDrawable(R.styleable.InteractiveButton_image)
                ?.let { imageView.setImageDrawable(it) }
        } finally {
            typedArray.recycle()
        }
        onState(State.ENABLED)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isClickable = enabled
        isFocusable
    }

    fun onState(state: State) =
        when (state) {
            State.ENABLED -> onEnabled()
            State.DISABLED -> onDisabled()
            State.RUNNING -> onRunning()
        }

    private fun onEnabled() {
        alpha = 1.0f
        this.isEnabled = true
        this.isActivated = false
    }

    private fun onDisabled() {
        alpha = 0.30f
        this.isEnabled = false
        this.isActivated = false
    }

    private fun onRunning() {
        alpha = 1.0f
        isActivated = true
        isActivated = true
    }

    enum class State {
        ENABLED,
        DISABLED,
        RUNNING
    }
}

