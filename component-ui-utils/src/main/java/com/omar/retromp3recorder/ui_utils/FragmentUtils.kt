package com.omar.retromp3recorder.ui_utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T : View> Fragment.findViewById(@IdRes id: Int): T = this.requireView().findViewById(id)