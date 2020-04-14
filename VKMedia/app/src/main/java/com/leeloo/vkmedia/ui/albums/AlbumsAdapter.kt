package com.leeloo.vkmedia.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.ui.AlbumViewHolder
import com.leeloo.vkmedia.ui.LoadingViewHolder
import com.leeloo.vkmedia.ui.MessageViewHolder
import com.leeloo.vkmedia.ui.RecyclerState
import com.leeloo.vkmedia.utils.AlbumsDiffCallback
import com.leeloo.vkmedia.vo.VKAlbum

class AlbumsAdapter(
    private val retryListener: () -> Unit,
    private val itemListener: (VKAlbum) -> Unit,
    private val removeListener: (VKAlbum) -> Unit,
    private val longListener: () -> Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<VKAlbum>()
    private var state = RecyclerState.LOADING
    private var isEditing = false

    fun setItems(items: List<VKAlbum>, state: RecyclerState) {
        if (state == RecyclerState.ITEM) {
            if (this.state == RecyclerState.ITEM) {
                this.state = state
            } else {
                this.state = state
                notifyItemRemoved(0)
            }
            val diffResult = DiffUtil.calculateDiff(
                AlbumsDiffCallback(
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

    fun setMode(isEditing: Boolean) {
        this.isEditing = isEditing
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) =
        when (state) {
            RecyclerState.ITEM -> position
            else -> state.viewType
        }

    override fun getItemCount() = if (items.isNotEmpty()) items.size else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (state) {
            RecyclerState.LOADING -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
            RecyclerState.ERROR -> MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_error,
                    parent,
                    false
                ),
                retryListener
            )
            RecyclerState.EMPTY -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_empty,
                    parent,
                    false
                )
            )
            RecyclerState.ITEM -> AlbumViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_album,
                    parent,
                    false
                ),
                itemListener,
                removeListener,
                longListener
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AlbumViewHolder)
            holder.bind(items[position], isEditing)
        else
            (holder.itemView.layoutParams
                    as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is AlbumViewHolder)
            holder.clear()
    }
}