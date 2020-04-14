package com.leeloo.vkmedia.ui

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.utils.isVisible
import com.leeloo.vkmedia.vo.VKAlbum
import com.leeloo.vkmedia.vo.VKPhoto

enum class RecyclerState(val viewType: Int) {
    LOADING(100000),
    ERROR(200000),
    EMPTY(300000),
    ITEM(1)
}

class AlbumViewHolder(
    view: View,
    itemListener: (VKAlbum) -> Unit,
    removeListener: (VKAlbum) -> Unit,
    longListener: () -> Boolean
) : RecyclerView.ViewHolder(view) {
    private lateinit var album: VKAlbum

    private val deleteImage = view.findViewById<ImageView>(R.id.delete_album)
    private val albumShadow = view.findViewById<ImageView>(R.id.system_album_shadow)
    private val albumImage = view.findViewById<ImageView>(R.id.album_preview)
    private val albumTitle = view.findViewById<TextView>(R.id.album_title)
    private val albumSize = view.findViewById<TextView>(R.id.album_size)

    init {
        view.setOnClickListener {
            itemListener(album)
        }
        view.setOnLongClickListener {
            longListener()
        }
        deleteImage.setOnClickListener {
            removeListener(album)
        }
    }

    fun bind(album: VKAlbum, isEditing: Boolean) {
        deleteImage.isVisible = isEditing && album.id >= 0
        albumShadow.isVisible = isEditing && album.id < 0
        this.album = album

        albumTitle.text = album.title
        albumSize.text = albumSize.resources
            .getQuantityString(
                R.plurals.item_album_size,
                album.size,
                album.size
            )

        Glide.with(albumShadow)
            .setDefaultRequestOptions(
                RequestOptions().transform(RoundedCorners(
                    albumShadow.resources.getDimensionPixelSize(R.dimen.gutter_default)
                ))
            )
            .load(ColorDrawable(
                ContextCompat.getColor(albumShadow.context, R.color.colorBackdrop)
            ))
            .into(albumShadow)

        Glide.with(albumImage)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.color.colorAccent)
                    .transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(
                                albumImage.resources.getDimensionPixelSize(R.dimen.gutter_default)
                            )
                        )
                    )
            )
            .load(album.previews.first { it.type == 'x' }.src)
            .into(albumImage)
    }

    fun clear() {
        Glide.with(albumImage)
            .clear(albumImage)
    }

}

class PhotoViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    private val image = view.findViewById<ImageView>(R.id.item_photo)

    fun bind(photo: VKPhoto) {
        Glide.with(image)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.color.colorAccent)
                    .centerCrop()
            )
            .load(photo.previews.first { it.type == 'x' }.src)
            .into(image)
    }

    fun clear() {
        Glide.with(image)
            .clear(image)
    }

}

class MessageViewHolder(
    view: View,
    listener: () -> Unit
) : RecyclerView.ViewHolder(view) {
    init {
        view.findViewById<MaterialButton>(R.id.retry_button).setOnClickListener { listener() }
    }
}

class TitleViewHolder(
    view: View,
    title: String
) : RecyclerView.ViewHolder(view) {
    init {
        (view as TextView).text = title
    }
}

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)