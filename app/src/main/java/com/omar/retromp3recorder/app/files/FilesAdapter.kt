package com.omar.retromp3recorder.app.files

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omar.retromp3recorder.app.R
import kotlinx.android.synthetic.main.item_file.view.*

class FilesAdapter(
        private val onDeleteListener: (String) -> Unit,
        private val onItemSelectListener: (String) -> Unit
) : RecyclerView.Adapter<FilesViewHolder>() {

    private lateinit var li: LayoutInflater
    private var data = listOf<FilesAdapterModel>()

    fun setData(fileNames: List<String>, selectedFileName: String) {
        data = fileNames.map { name ->
            FilesAdapterModel(name, name == selectedFileName)
        }
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val context = recyclerView.context
        li = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): FilesViewHolder {
        val view = li.inflate(R.layout.item_file, parent, false)
        return FilesViewHolder(onDeleteListener, onItemSelectListener, view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(viewHolder: FilesViewHolder, index: Int) {
        viewHolder.bind(data[index])
    }
}

data class FilesAdapterModel(val fileName: String, val isSelected: Boolean)

class FilesViewHolder(
        private val onDeleteListener: (String) -> Unit,
        private val onItemSelectListener: (String) -> Unit,
        itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: FilesAdapterModel) {
        itemView.setOnClickListener { onItemSelectListener.invoke(data.fileName) }
        val last = data.fileName.split("/").last()
        itemView.textView.text = last
        itemView.closeBtn.setOnClickListener { onDeleteListener.invoke(data.fileName) }
        itemView.alpha = if (data.isSelected) 0.1f else 1.0f
    }
}