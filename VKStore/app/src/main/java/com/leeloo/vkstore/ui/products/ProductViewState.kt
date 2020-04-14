package com.leeloo.vkstore.ui.products

import com.leeloo.vkstore.vo.VKProduct

data class ProductViewState(
    val products: List<VKProduct>,
    val isProductsLoading: Boolean,
    val errorLoadingProducts: Exception?,
    val isRefreshLoading: Boolean,
    val errorRefreshLoading: Exception?
) {
    companion object {
        fun init() = ProductViewState(
            products = emptyList(),
            isProductsLoading = false,
            errorLoadingProducts = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun loadingProducts() = ProductViewState(
            products = emptyList(),
            isProductsLoading = true,
            errorLoadingProducts = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun productsLoaded(products: List<VKProduct>) = ProductViewState(
            products = products,
            isProductsLoading = false,
            errorLoadingProducts = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun productsLoadingError(error: Exception) = ProductViewState(
            products = emptyList(),
            isProductsLoading = false,
            errorLoadingProducts = error,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun refreshLoading(products: List<VKProduct>) = ProductViewState(
            products = products,
            isProductsLoading = false,
            errorLoadingProducts = null,
            isRefreshLoading = true,
            errorRefreshLoading = null
        )

        fun refreshLoadingError(
            products: List<VKProduct>,
            error: Exception
        ) = ProductViewState(
            products = products,
            isProductsLoading = false,
            errorLoadingProducts = null,
            isRefreshLoading = false,
            errorRefreshLoading = error
        )

    }
}