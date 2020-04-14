package com.leeloo.vkdocuments.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leeloo.vkdocuments.R
import com.leeloo.vkdocuments.utils.DocumentsDiffCallback
import com.leeloo.vkdocuments.vo.VKDocument

enum class RecyclerState(val viewType: Int) {
    NOT_LOGGED(3000),
    LOADING(4000),
    DOCS(0),
    EMPTY(5000),
    ERROR(6000)
}

class DocumentAdapter(
    private val menuListener: (Int, VKDocument) -> Boolean,
    private val loginListener: () -> Unit,
    private val reloadListener: () -> Unit,
    private val itemListener: (VKDocument) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<VKDocument> = mutableListOf()
    private var state: RecyclerState = RecyclerState.NOT_LOGGED

    private val layoutId: Int
        get() =
            when (state) {
                RecyclerState.NOT_LOGGED -> R.layout.item_login
                RecyclerState.LOADING -> R.layout.item_loading
                RecyclerState.DOCS -> R.layout.item_document
                RecyclerState.EMPTY -> R.layout.item_empty
                else -> R.layout.item_error
            }

    private val buttonId: Int
        get() =
            when (state) {
                RecyclerState.NOT_LOGGED -> R.id.login_button
                else -> R.id.retry_button
            }

    fun updateData(items: List<VKDocument>, state: RecyclerState) {
        if (state == RecyclerState.DOCS) {
            if (this.state == RecyclerState.DOCS) {
                this.state = state
            } else {
                this.state = state
                notifyItemRemoved(0)
            }
            val diffResult = DiffUtil.calculateDiff(
                DocumentsDiffCallback(
                    this.items,
                    items
                )
            )
            this.items.clear()
            this.items.addAll(items)
            diffResult.dispatchUpdatesTo(this)
        } else {
            if (this.state == RecyclerState.DOCS) {
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

    override fun getItemCount() = if (items.isNotEmpty()) items.size else 1

    override fun getItemViewType(position: Int) =
        if (position == 0) state.viewType else position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (state) {
            RecyclerState.DOCS ->
                DocumentViewHolder(
                    view = LayoutInflater.from(parent.context).inflate(
                        layoutId,
                        parent,
                        false
                    ),
                    itemListener = itemListener,
                    listener = menuListener
                )
            RecyclerState.LOADING, RecyclerState.EMPTY ->
                LoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        layoutId,
                        parent,
                        false
                    )
                )
            else ->
                MessageViewHolder(
                    view = LayoutInflater.from(parent.context).inflate(
                        layoutId,
                        parent,
                        false
                    ),
                    buttonId = buttonId,
                    listener = if (state == RecyclerState.NOT_LOGGED) loginListener else reloadListener
                )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DocumentViewHolder)
            holder.bind(items[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is DocumentViewHolder)
            holder.clear()
    }

}