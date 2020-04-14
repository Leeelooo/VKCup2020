package com.leeloo.vkdocuments.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkdocuments.requests.GetUserRequest
import com.leeloo.vkdocuments.ui.DocumentModelState
import com.leeloo.vkdocuments.vo.VKUser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class UserRepository {
    private val _liveData = MutableLiveData<DocumentModelState>()
    val liveData: LiveData<DocumentModelState>
        get() = _liveData

    init {
        clear()
    }

    fun requestUser(
        userIds: IntArray = intArrayOf(),
        fields: String = "photo_200"
    ) {
        VK.execute(
            GetUserRequest(userIds, fields), object : VKApiCallback<VKUser> {
                override fun fail(error: Exception) {
                    if (VK.isLoggedIn())
                        _liveData.postValue(DocumentModelState.UserLoadingFailed(error))
                }

                override fun success(result: VKUser) {
                    _liveData.postValue(DocumentModelState.UserLoaded(result))
                }
            })
    }

    fun userLoggedIn() {
        _liveData.value = DocumentModelState.UserLoggedIn
        requestUser()
    }

    fun clear() {
        _liveData.value = DocumentModelState.Init
    }

    companion object {
        private lateinit var _instance: UserRepository
        fun getInstance(): UserRepository {
            if (!::_instance.isInitialized)
                _instance = UserRepository()
            return _instance
        }
    }
}