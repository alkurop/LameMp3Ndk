package com.omar.retromp3recorder.app

import android.app.Service
import android.content.Intent
import android.os.IBinder

class WakelockService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}