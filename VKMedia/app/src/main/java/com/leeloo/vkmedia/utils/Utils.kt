package com.leeloo.vkmedia.utils

import android.content.Context
import android.net.Uri
import android.view.View
import java.io.File
import java.io.FileOutputStream

var View.isVisible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

fun getPath(context: Context, uri: Uri?): Uri? {
    if (uri == null) return null
    val inputStream = context.contentResolver.openInputStream(uri)
    val cacheFile = File(context.cacheDir, "cache.jpg")
    val outputStream = FileOutputStream(cacheFile)
    inputStream?.copyTo(outputStream)
    return Uri.parse("file://" + cacheFile.path)
}