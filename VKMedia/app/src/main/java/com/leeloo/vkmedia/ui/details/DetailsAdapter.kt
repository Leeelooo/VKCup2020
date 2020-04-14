package com.leeloo.vkmedia.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.ui.*
import com.leeloo.vkmedia.utils.PhotosDiffCallback
import com.leeloo.vkmedia.vo.VKPhoto

class DetailsAdapter(
    private val retryListener: () -> Unit,
    private val title: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<VKPhoto>()
    private var state = RecyclerState.LOADING

    fun setItems(items: List<VKPhoto>, state: RecyclerState) {
        if (state == RecyclerState.ITEM) {
            if (this.state == RecyclerState.ITEM) {
                this.state = state
            } else {
                this.state = state
                notifyItemRemoved(1)
            }
            val diffResult = DiffUtil.calculateDiff(
                PhotosDiffCallback(
                    this.items,
                    items
                )
            )
            this.items.clear()
            this.items.addAll(items)
            diffResult.dispatchUpdatesTo(this)
        } else {
            if (this.state == RecyclerState.ITEM) {
                notifyItemRangeRemoved(1, itemCount - 1)
                this.items.clear()
                this.state = state
                notifyItemInserted(1)
            } else {
                this.state = state
                notifyItemChanged(1)
            }
        }
    }

    override fun getItemCount() = if (items.isNotEmpty()) items.size + 1 else 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when {
            viewType == 0 -> TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_album_title,
                    parent,
                    false
                ),
                title
            )
            state == RecyclerState.LOADING -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
            state == RecyclerState.ERROR -> MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_error,
                    parent,
                    false
                ),
                retryListener
            )
            state == RecyclerState.EMPTY -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_empty,
                    parent,
                    false
                )
            )
            else -> PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_photo,
                    parent,
                    false
                )
            )
        }

    override fun getItemViewType(position: Int) =
        when {
            position == 0 -> 0
            state == RecyclerState.ITEM -> position
            else -> state.viewType
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoViewHolder)
            holder.bind(items[position - 1])
        else
            (holder.itemView.layoutParams
                    as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is PhotoViewHolder)
            holder.clear()
    }
}