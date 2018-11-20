package com.omar.retromp3recorder.app.files

import android.support.v7.util.DiffUtil

class FilesDiffUtilCallback(
        val oldList: List<FilesAdapterModel>,
        val newList: List<FilesAdapterModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(index1: Int, index2: Int): Boolean {
        return oldList[index1].fileName == newList[index2].fileName
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(index1: Int, index2: Int): Boolean {
        return oldList[index1] == newList[index2]
    }
}