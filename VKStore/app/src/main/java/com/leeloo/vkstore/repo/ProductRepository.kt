package com.leeloo.vkstore.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkstore.requests.GetGroupProductsRequest
import com.leeloo.vkstore.ui.products.ProductModelState
import com.leeloo.vkstore.vo.VKProduct
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class ProductRepository {
    private val _modelState = MutableLiveData<ProductModelState>()
    val modelState: LiveData<ProductModelState>
        get() = _modelState

    init {
        _modelState.value = ProductModelState.Init
    }

    fun loadProducts(
        ownerId: Long,
        count: Int = 200,
        isRefresh: Boolean = false
    ) {
        _modelState.value =
            if (isRefresh) ProductModelState.RefreshLoading
            else ProductModelState.LoadingProducts
        VK.execute(
            GetGroupProductsRequest(ownerId * -1, count),
            object : VKApiCallback<List<VKProduct>> {
                override fun fail(error: Exception) {
                    _modelState.postValue(
                        if (isRefresh) ProductModelState.RefreshLoadingError(error)
                        else ProductModelState.ProductsLoadingError(error)
                    )
                }

                override fun success(result: List<VKProduct>) {
                    _modelState.postValue(ProductModelState.ProductsLoaded(result))
                }

            })
    }

    companion object {
        private lateinit var _instance: ProductRepository
        fun getInstance(): ProductRepository {
            if (!::_instance.isInitialized)
                _instance = ProductRepository()
            return _instance
        }
    }
}