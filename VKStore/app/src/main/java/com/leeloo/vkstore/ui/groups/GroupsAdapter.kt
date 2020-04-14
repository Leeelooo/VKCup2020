package com.leeloo.vkstore.ui.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leeloo.vkstore.R
import com.leeloo.vkstore.ui.GroupViewHolder
import com.leeloo.vkstore.ui.RecyclerState
import com.leeloo.vkstore.ui.RetryViewHolder
import com.leeloo.vkstore.ui.ViewHolder
import com.leeloo.vkstore.utils.GroupDiffCallback
import com.leeloo.vkstore.vo.VKGroup

class GroupsAdapter(
    private val itemListener: (VKGroup) -> Unit,
    private val retryListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<VKGroup>()
    private var state = RecyclerState.LOADING

    fun setItems(items: List<VKGroup>, state: RecyclerState) {
        if (state == RecyclerState.ITEM) {
            if (this.state == RecyclerState.ITEM) {
                this.state = state
            } else {
                this.state = state
                notifyItemRemoved(0)
            }
            val diffResult = DiffUtil.calculateDiff(
                GroupDiffCallback(
                    this.items,
                    items
                )
            )
            this.items.clear()
            this.items.addAll(items)
            diffResult.dispatchUpdatesTo(this)
        } else {
            if (this.state == RecyclerState.ITEM) {
                notifyItemRangeRemoved(0, itemCount)
                this.items.clear()
                this.state = state
                notifyItemInserted(0)
            } else {
                this.state = state
                notifyItemChanged(0)
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (state == RecyclerState.ITEM) position
        else state.viewType

    override fun getItemCount() = if (items.isNotEmpty()) items.size else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (state) {
            RecyclerState.LOADING -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_loading,
                        parent,
                        false
                    )
            )
            RecyclerState.ERROR -> RetryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_error,
                        parent,
                        false
                    ),
                retryListener
            )
            RecyclerState.EMPTY -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_empty,
                        parent,
                        false
                    )
            )
            RecyclerState.ITEM -> GroupViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_group,
                        parent,
                        false
                    ),
                itemListener
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupViewHolder)
            holder.bind(items[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is GroupViewHolder)
            holder.clear()
    }
}