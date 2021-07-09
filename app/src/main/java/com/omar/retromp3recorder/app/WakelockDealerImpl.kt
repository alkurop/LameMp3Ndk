package com.omar.retromp3recorder.app

import android.content.Context
import android.content.Intent
import android.os.Build
import com.omar.retromp3recorder.utils.WakeLockDealer
import javax.inject.Inject

class WakelockDealerImpl @Inject constructor(
    private val context: Context
) : WakeLockDealer {
    override fun open() {
        with(context) {
            val startIntent = Intent(this, WakelockService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
    }
}