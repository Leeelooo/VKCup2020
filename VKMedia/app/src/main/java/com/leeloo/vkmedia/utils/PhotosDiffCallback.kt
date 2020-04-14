package com.leeloo.vkmedia.utils

import androidx.recyclerview.widget.DiffUtil
import com.leeloo.vkmedia.vo.VKPhoto

class PhotosDiffCallback(
    private val old: List<VKPhoto>,
    private val new: List<VKPhoto>
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].id == new[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]
}