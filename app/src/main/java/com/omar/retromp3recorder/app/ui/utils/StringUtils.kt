package com.omar.retromp3recorder.app.ui.utils

fun String.fileName(): String = this.split("/").last().split(".").first()