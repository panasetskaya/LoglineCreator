package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import androidx.recyclerview.widget.DiffUtil
import com.panasetskaia.storyarchitectlogline.domain.Logline

class LoglineDiffUtilCallback: DiffUtil.ItemCallback<Logline>() {
    override fun areItemsTheSame(oldItem: Logline, newItem: Logline): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Logline, newItem: Logline): Boolean {
        return oldItem == newItem
    }
}
