package com.leeloo.vkstore.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.leeloo.vkstore.R
import com.leeloo.vkstore.utils.CircularOutlineProvider
import com.leeloo.vkstore.utils.SmoothOutlineProvider
import com.leeloo.vkstore.utils.isVisible
import com.leeloo.vkstore.vo.VKCity
import com.leeloo.vkstore.vo.VKGroup
import com.leeloo.vkstore.vo.VKProduct

enum class RecyclerState(val viewType: Int) {
    LOADING(1000000),
    ERROR(2000000),
    ITEM(0),
    EMPTY(3000000)
}

class ProductViewHolder(
    view: View,
    clickListener: (VKProduct) -> Unit
) : RecyclerView.ViewHolder(view) {
    private lateinit var product: VKProduct

    private val productImage = view.findViewById<ImageView>(R.id.product_image)
    private val productTitle = view.findViewById<TextView>(R.id.product_title)
    private val productPrice = view.findViewById<TextView>(R.id.product_price)

    init {
        view.setOnClickListener { clickListener(product) }
    }

    fun bind(product: VKProduct) {
        this.product = product

        productImage.outlineProvider = SmoothOutlineProvider
        productImage.clipToOutline = true
        Glide.with(productImage)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.color.colorAccent)
            )
            .load(product.photoUrl)
            .into(productImage)
        productTitle.text = product.title
        productPrice.text = product.price
    }

    fun clear() {
        Glide.with(productImage)
            .clear(productImage)
    }

}

class GroupViewHolder(
    view: View,
    clickListener: (VKGroup) -> Unit
) : RecyclerView.ViewHolder(view) {
    private lateinit var group: VKGroup

    private val groupImage = view.findViewById<ImageView>(R.id.group_photo)
    private val groupTitle = view.findViewById<TextView>(R.id.product_title_toolbar)
    private val groupType = view.findViewById<TextView>(R.id.group_type)

    init {
        view.setOnClickListener { clickListener(group) }
    }

    fun bind(group: VKGroup) {
        this.group = group

        groupImage.outlineProvider = CircularOutlineProvider
        groupImage.clipToOutline = true
        Glide.with(groupImage)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.color.colorAccent)
            )
            .load(group.photoUrl)
            .into(groupImage)
        groupTitle.text = group.name
        groupType.text = groupType.resources.getString(
            when (group.type) {
                "group" -> R.string.type_group
                "page" -> R.string.type_page
                else -> R.string.type_event
            }
        )
    }

    fun clear() {
        Glide.with(groupImage)
            .clear(groupImage)
    }

}

class CityViewHolder(
    view: View,
    clickListener: (VKCity) -> Unit
) : RecyclerView.ViewHolder(view) {
    private lateinit var city: VKCity

    private val cityTitle = view.findViewById<TextView>(R.id.city_title_toolbar)
    private val checkImage = view.findViewById<ImageView>(R.id.icon_city_checked)

    init {
        view.setOnClickListener { clickListener(city) }
    }

    fun bind(city: VKCity, checkedCity: VKCity?) {
        this.city = city

        cityTitle.text = city.name
        checkImage.isVisible = city == checkedCity
    }

}

class RetryViewHolder(
    view: View,
    retryListener: () -> Unit
) : RecyclerView.ViewHolder(view) {
    init {
        view.findViewById<MaterialButton>(R.id.retry_button).setOnClickListener {
            retryListener()
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)