package com.leeloo.vkshare

import android.net.Uri

data class ViewState(
    val isLoggedIn: Boolean,
    val photoUri: Uri?,
    val isSended: Int
) {

    companion object {
        fun init() = ViewState(false, null, 0)
        fun onLogin() = ViewState(true, null, 0)
        fun onImageChoosed(uri: Uri?) = ViewState(true, uri, 0)
        fun onPostShared(uri: Uri?) = ViewState(true, uri, 1)
        fun onPostShareError(uri: Uri?) = ViewState(true, uri, 2)
    }

}