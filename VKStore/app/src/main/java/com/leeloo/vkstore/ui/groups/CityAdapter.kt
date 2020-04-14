package com.leeloo.vkstore.ui.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leeloo.vkstore.R
import com.leeloo.vkstore.ui.CityViewHolder
import com.leeloo.vkstore.ui.RecyclerState
import com.leeloo.vkstore.ui.RetryViewHolder
import com.leeloo.vkstore.ui.ViewHolder
import com.leeloo.vkstore.utils.CityDiffCallback
import com.leeloo.vkstore.vo.VKCity

class CityAdapter(
    private val itemListener: (VKCity) -> Unit,
    private val retryListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<VKCity>()
    private var state = RecyclerState.LOADING
    private var choosedCity: VKCity? = null

    fun setItems(items: List<VKCity>, state: RecyclerState, choosedCity: VKCity?) {
        if (this.choosedCity != null) {
            val index = items.indexOf(this.choosedCity!!)
            this.choosedCity = choosedCity
            notifyItemChanged(index)
            if (items.isNotEmpty())
                notifyItemChanged(items.indexOf(choosedCity))
        } else {
            this.choosedCity = choosedCity
            if (items.isNotEmpty())
                notifyItemChanged(items.indexOf(choosedCity))
        }
        if (state == RecyclerState.ITEM) {
            if (this.state == RecyclerState.ITEM) {
                this.state = state
            } else {
                this.state = state
                notifyItemRemoved(0)
            }
            val diffResult = DiffUtil.calculateDiff(
                CityDiffCallback(
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
            RecyclerState.ITEM -> CityViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_city,
                        parent,
                        false
                    ),
                itemListener
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CityViewHolder)
            holder.bind(items[position], choosedCity)
    }

}