package com.leeloo.vkshare

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class StateViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    fun onLogin() {
        _viewState.value = ViewState.onLogin()
    }

    fun onLogout() {
        _viewState.value = ViewState.init()
    }

    fun onDismiss() {
        _viewState.value = ViewState.onLogin()
    }

    fun onSendClicked(message: String, context: Context) {
        VK.execute(
            VKWallPostCommand(
                message,
                PathUtils.getPath(context, _viewState.value?.photoUri!!)
            ),
            object : VKApiCallback<Int> {
                override fun success(result: Int) {
                    _viewState.value = ViewState.onPostShared(_viewState.value?.photoUri)
                }

                override fun fail(error: Exception) {
                    _viewState.value = ViewState.onPostShareError(_viewState.value?.photoUri)
                }
            })
        _viewState.value = ViewState.onLogin()
    }

    fun onImageChoosed(photoUri: Uri?) {
        _viewState.value = ViewState.onImageChoosed(photoUri)
    }

}