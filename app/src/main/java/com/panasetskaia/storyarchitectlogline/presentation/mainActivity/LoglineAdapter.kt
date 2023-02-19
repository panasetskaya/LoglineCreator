package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import android.R.attr.data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.storyarchitectlogline.R
import com.panasetskaia.storyarchitectlogline.databinding.ItemLoglineBinding
import com.panasetskaia.storyarchitectlogline.domain.Logline
import java.util.*


class LoglineAdapter(val context: MainActivity) :
    ListAdapter<Logline, LoglineAdapter.LoglineViewHolder>(LoglineDiffUtilCallback()),
    SwipeHelper.ItemTouchHelperContract {

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

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(currentList.toMutableList(), i, i + 1)
                changeOrderOfItem(getItem(i))
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(currentList.toMutableList(), i, i - 1)
                changeOrderOfItem(getItem(i))
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    private fun changeOrderOfItem(item: Logline) {
        Toast.makeText(context, "Changing order in Database!", Toast.LENGTH_SHORT).show()
        //todo: implement changing order in ViewModel->Dao
    }

}