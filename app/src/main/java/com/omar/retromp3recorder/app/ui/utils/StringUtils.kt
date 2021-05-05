package com.omar.retromp3recorder.app.ui.utils

fun String.toFileName(): String = this.split("/").last().split(".").first()