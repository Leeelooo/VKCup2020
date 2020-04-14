package com.leeloo.vkunsub.utils

import androidx.recyclerview.widget.DiffUtil
import com.leeloo.vkunsub.vo.VKGroup

class GroupDiffCallback(
    private val oldList: List<VKGroup>,
    private val newList: List<VKGroup>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}