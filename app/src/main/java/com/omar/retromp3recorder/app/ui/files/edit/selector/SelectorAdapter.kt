package com.omar.retromp3recorder.app.ui.files.edit.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById

class SelectorAdapter(
    val onItemSelectedListener: (SelectorView.Item) -> Unit
) : RecyclerView.Adapter<SelectorAdapter.SelectorViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    var items: List<SelectorView.Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val view = layoutInflater.inflate(R.layout.selector_item_view, parent, false)
        return SelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currentFileSign: View
            get() = findViewById(R.id.current_file_sign)
        private val textView: TextView
            get() = findViewById(R.id.current_file_text)

        fun bind(item: SelectorView.Item) {
            itemView.setOnClickListener { onItemSelectedListener(item) }
            textView.text = item.fileName
            currentFileSign.isVisible = item.isCurrentItem
        }
    }
}