package com.omar.retromp3recorder.app

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.omar.retromp3recorder.utils.WakeLockDealer
import javax.inject.Inject

class WakelockService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class WakelockDealerImpl @Inject constructor(
    private val context: Context
) : WakeLockDealer {
    override fun open() {
    }

    override fun close() {
    }
}