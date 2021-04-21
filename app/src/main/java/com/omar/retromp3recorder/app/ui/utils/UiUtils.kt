package com.omar.retromp3recorder.app.ui.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun <T : View> Fragment.findViewById(@IdRes id: Int): T = this.requireView().findViewById(id)

fun <T : View> RecyclerView.ViewHolder.findViewById(@IdRes id: Int): T =
    this.itemView.findViewById(id)