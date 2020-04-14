package com.leeloo.vkunsub.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leeloo.vkunsub.R
import com.leeloo.vkunsub.utils.GroupDiffCallback
import com.leeloo.vkunsub.vo.VKGroup

class GroupAdapter(
    private val itemListener: (VKGroup) -> Unit,
    private val longListener: (VKGroup) -> Boolean,
    private val loginListener: () -> Unit,
    private val retryListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<VKGroup>()
    private var state = RecyclerState.LOADING
    private val selected = hashSetOf<VKGroup>()

    fun setItems(items: List<VKGroup>, state: RecyclerState, selected: HashSet<VKGroup>) {
        items.forEachIndexed { index, group ->
            if (selected.contains(group) && !this.selected.contains(group))
                notifyItemChanged(index)
            if (!selected.contains(group) && this.selected.contains(group))
                notifyItemChanged(index)
        }
        this.selected.clear()
        this.selected.addAll(selected)
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
                retryListener,
                R.id.retry_button
            )
            RecyclerState.LOGIN -> RetryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_login,
                        parent,
                        false
                    ),
                loginListener,
                R.id.login_button
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
                itemListener,
                longListener
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupViewHolder)
            holder.bind(items[position], selected.contains(items[position]))
        else
            (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                .isFullSpan = true
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is GroupViewHolder)
            holder.clear()
    }
}