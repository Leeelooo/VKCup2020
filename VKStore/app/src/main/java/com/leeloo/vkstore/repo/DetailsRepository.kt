package com.leeloo.vkstore.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkstore.requests.PostFaveAddProductRequest
import com.leeloo.vkstore.requests.PostFaveRemoveProductRequest
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class DetailsRepository {
    private val _faveData = MutableLiveData<Boolean>()
    val faveData: LiveData<Boolean>
        get() = _faveData

    fun onSetFave(
        ownerId: Long,
        productId: Long
    ) {
        VK.execute(PostFaveAddProductRequest(ownerId, productId), object : VKApiCallback<Boolean> {
            override fun fail(error: Exception) {

            }

            override fun success(result: Boolean) {
                if (result)
                    _faveData.postValue(true)
            }

        })
    }

    fun onRemoveFave(
        ownerId: Long,
        productId: Long
    ) {
        VK.execute(
            PostFaveRemoveProductRequest(ownerId, productId),
            object : VKApiCallback<Boolean> {
                override fun fail(error: Exception) {

                }

                override fun success(result: Boolean) {
                    if (result)
                        _faveData.postValue(false)
                }
            })
    }

    fun initFaveValue(init: Boolean) {
        _faveData.value = init
    }

    companion object {
        private lateinit var _instance: DetailsRepository
        fun getInstance(): DetailsRepository {
            if (!::_instance.isInitialized)
                _instance = DetailsRepository()
            return _instance
        }
    }
}