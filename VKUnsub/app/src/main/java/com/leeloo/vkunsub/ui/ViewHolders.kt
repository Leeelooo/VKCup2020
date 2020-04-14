package com.leeloo.vkunsub.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.leeloo.vkunsub.R
import com.leeloo.vkunsub.utils.CircularOutlineProvider
import com.leeloo.vkunsub.utils.isVisible
import com.leeloo.vkunsub.vo.VKGroup

enum class RecyclerState(val viewType: Int) {
    LOADING(10000),
    ITEM(0),
    ERROR(20000),
    EMPTY(30000),
    LOGIN(40000)
}

class GroupViewHolder(
    view: View,
    clickListener: (VKGroup) -> Unit,
    longListener: (VKGroup) -> Boolean
) : RecyclerView.ViewHolder(view) {
    private lateinit var group: VKGroup

    private val groupImage = view.findViewById<ImageView>(R.id.group_image)
    private val groupTitle = view.findViewById<TextView>(R.id.group_title)

    private val imageOutline = view.findViewById<ImageView>(R.id.group_image_outline)
    private val check = view.findViewById<ImageView>(R.id.group_image_marked)
    private val checkBackground = view.findViewById<ImageView>(R.id.group_image_marked_outline)

    init {
        view.setOnClickListener { clickListener(group) }
        view.setOnLongClickListener { longListener(group) }
    }

    fun bind(group: VKGroup, checked: Boolean) {
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
        imageOutline.isVisible = checked
        check.isVisible = checked
        checkBackground.isVisible = checked
    }

    fun clear() {
        Glide.with(groupImage)
            .clear(groupImage)
    }

}

class RetryViewHolder(
    view: View,
    retryListener: () -> Unit,
    buttonId: Int
) : RecyclerView.ViewHolder(view) {
    init {
        view.findViewById<MaterialButton>(buttonId).setOnClickListener {
            retryListener()
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
