package com.leeloo.vkdocuments.utils

import android.content.Context
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.leeloo.vkdocuments.R
import com.leeloo.vkdocuments.vo.Photo
import com.leeloo.vkdocuments.vo.VKDocument

fun Long.formatFileSize(context: Context): String {
    var times = 0
    var currentSize = this.toDouble()
    while (currentSize / 1024 > 1.0 && times < 3) {
        currentSize /= 1024
        times++
    }
    return context.resources.getString(
        when (times) {
            0 -> R.string.filesize_bytes
            1 -> R.string.filesize_kilobytes
            2 -> R.string.filesize_megabytes
            else -> R.string.filesize_bytes
        },
        currentSize
    )
}

var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

fun VKDocument.getBackgroundColor(context: Context) =
    AppCompatResources.getDrawable(
        context,
        when (this.type) {
            1 -> R.drawable.ic_txt
            2 -> R.drawable.ic_zip
            3, 4 -> R.color.colorAccent
            5 -> R.drawable.ic_music
            6 -> R.drawable.ic_video
            7 -> R.drawable.ic_book
            else -> R.drawable.ic_other
        }
    )

fun List<Photo>.getBestOption() = this.first { it.type == 'm' }.src