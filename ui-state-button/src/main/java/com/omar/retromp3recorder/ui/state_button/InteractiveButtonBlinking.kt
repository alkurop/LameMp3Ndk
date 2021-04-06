package com.omar.retromp3recorder.ui.state_button

import android.content.Context
import android.util.AttributeSet
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class InteractiveButtonBlinking @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InteractiveButton(context, attrs, defStyleAttr) {
    override fun onRunning() {
        super.onRunning()
        Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isActivated = !isActivated
            }

            .disposedBy(compositeDisposable)
    }
}

