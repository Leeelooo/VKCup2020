package com.leeloo.vkdocuments.ui

import android.os.Build
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.leeloo.vkdocuments.R
import com.leeloo.vkdocuments.utils.*
import com.leeloo.vkdocuments.vo.VKDocument


class DocumentViewHolder(
    view: View,
    itemListener: (VKDocument) -> Unit,
    listener: (Int, VKDocument) -> Boolean
) : RecyclerView.ViewHolder(view) {
    private val preview = view.findViewById<ImageView>(R.id.item_document_preview)
    private val more = view.findViewById<ImageView>(R.id.item_document_more)
    private val title = view.findViewById<TextView>(R.id.item_document_title)
    private val info = view.findViewById<TextView>(R.id.item_document_info)
    private val tags = view.findViewById<TextView>(R.id.item_document_tags)

    private lateinit var document: VKDocument

    init {
        preview.outlineProvider = SmoothOutlineProvider
        preview.clipToOutline = true
        more.setOnClickListener { _ ->
            val popup = PopupMenu(view.context, more)
            popup.inflate(R.menu.menu_document_item_dropdown)
            popup.setOnMenuItemClickListener { listener(it.itemId, document) }
            popup.show()
        }
        view.setOnClickListener { itemListener(document) }
    }

    fun bind(document: VKDocument) {
        this.document = document

        title.text = document.title
        info.text = info.resources.getString(
            R.string.document_item_info_template,
            document.ext.toUpperCase(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    info.resources.configuration.locales[0]
                else
                    info.resources.configuration.locale
            ),
            document.size.formatFileSize(info.context),
            DateUtils.getRelativeTimeSpanString(
                document.date * 1000,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        )
        tags.visible = document.tags.isNotEmpty()
        tags.text = document.tags
            .map { it }
            .joinToString { it }

        if (document.preview.isNotEmpty())
            Glide.with(preview)
                .applyDefaultRequestOptions(
                    RequestOptions
                        .placeholderOf(document.getBackgroundColor(preview.context))
                        .centerCrop()
                )
                .load(document.preview.getBestOption())
                .into(preview)
        else
            Glide.with(preview)
                .load(document.getBackgroundColor(preview.context))
                .into(preview)
    }


    fun clear() {
        Glide.with(preview).clear(preview)
    }

}

class MessageViewHolder(
    view: View,
    buttonId: Int,
    listener: () -> Unit
) : RecyclerView.ViewHolder(view) {
    init {
        view.findViewById<MaterialButton>(buttonId).setOnClickListener { listener() }
    }
}

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)