package com.leeloo.vkdocuments.utils

import androidx.recyclerview.widget.DiffUtil
import com.leeloo.vkdocuments.vo.VKDocument

class DocumentsDiffCallback(
    private val oldList: List<VKDocument>,
    private val newList: List<VKDocument>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}