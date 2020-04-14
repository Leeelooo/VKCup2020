package com.leeloo.vkstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkstore.repo.DetailsRepository

class DetailsViewModel : ViewModel() {
    private val repo = DetailsRepository.getInstance()
    val faveState: LiveData<Boolean>
        get() = repo.faveData

    fun onAddToFavorites(
        ownerId: Long,
        productId: Long
    ) {
        repo.onSetFave(ownerId, productId)
    }

    fun onRemoveFromFavorites(
        ownerId: Long,
        productId: Long
    ) {
        repo.onRemoveFave(ownerId, productId)
    }

    fun setDefaultValue(isFavorite: Boolean) {
        repo.initFaveValue(isFavorite)
    }

}