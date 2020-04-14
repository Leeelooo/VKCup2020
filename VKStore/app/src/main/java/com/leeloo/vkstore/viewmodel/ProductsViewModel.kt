package com.leeloo.vkstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkstore.repo.ProductRepository
import com.leeloo.vkstore.ui.products.ProductViewState

class ProductsViewModel : ViewModel() {
    private val repo = ProductRepository.getInstance()
    private val _viewState = MediatorLiveData<ProductViewState>()
    val viewState: LiveData<ProductViewState>
        get() = _viewState

    init {
        _viewState.value = ProductViewState.init()
        _viewState.addSource(repo.modelState) {
            _viewState.value = it.reduce(_viewState.value!!)
        }
    }

    fun onRetry(groupId: Long) {
        repo.loadProducts(groupId)
    }

    fun onRefresh(groupId: Long) {
        repo.loadProducts(groupId, isRefresh = true)
    }

}