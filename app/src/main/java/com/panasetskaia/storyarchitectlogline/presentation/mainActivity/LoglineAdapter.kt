package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.ItemLoglineBinding
import com.panasetskaia.storyarchitectlogline.domain.Logline

class LoglineAdapter(val context: MainActivity, val viewModel: MainViewModel) :
    ListAdapter<Logline, LoglineAdapter.LoglineViewHolder>(LoglineDiffUtilCallback()) {

    var onCharacterItemClickListener: ((Logline) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoglineViewHolder {
        val binding = ItemLoglineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoglineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoglineViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.root.setOnClickListener {
            onCharacterItemClickListener?.invoke(item)
            true
        }
        with(binding) {
            tvLogline.text = item.text
            tvDate.text = item.date
            tvWordCount.text = String.format(
                context.getString(R.string.words_count),
                item.count
            )
        }
    }

    class LoglineViewHolder(val binding: ItemLoglineBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun deleteItemOnPosition(position: Int) {
        val item = getItem(position)
        viewModel.deleteLogline(item.id)
    }
}