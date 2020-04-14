package com.leeloo.vkstore.ui.products

import com.leeloo.vkstore.vo.VKProduct
import java.lang.Exception

sealed class ProductModelState {
    abstract fun reduce(oldState: ProductViewState): ProductViewState

    object Init : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.init()
    }

    object LoadingProducts : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.loadingProducts()
    }

    class ProductsLoaded(
        private val products: List<VKProduct>
    ) : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.productsLoaded(products)
    }

    class ProductsLoadingError(
        private val error: Exception
    ) : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.productsLoadingError(error)
    }

    object RefreshLoading : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.refreshLoading(oldState.products)
    }

    class RefreshLoadingError(
        private val error: Exception
    ) : ProductModelState() {
        override fun reduce(oldState: ProductViewState) =
            ProductViewState.refreshLoadingError(oldState.products, error)
    }


}