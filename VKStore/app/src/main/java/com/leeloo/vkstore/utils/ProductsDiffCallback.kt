package com.leeloo.vkstore.utils

import androidx.recyclerview.widget.DiffUtil
import com.leeloo.vkstore.vo.VKProduct

class ProductsDiffCallback(
    private val oldList: List<VKProduct>,
    private val newList: List<VKProduct>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}