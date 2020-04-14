package com.leeloo.vkmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkmedia.repo.DetailsRepository
import com.leeloo.vkmedia.ui.details.DetailsViewState

class DetailsViewModel : ViewModel() {
    private val repo = DetailsRepository.getInstance()

    private val _viewStateData = MediatorLiveData<DetailsViewState>()
    val viewStateData: LiveData<DetailsViewState>
        get() = _viewStateData

    init {
        _viewStateData.value = DetailsViewState.init()
        _viewStateData.addSource(repo.repoState) {
            _viewStateData.value = it.reduce(_viewStateData.value!!)
        }
    }

    fun onRetry(id: Long) {
        repo.loadPhotos(albumId = id)
    }

    fun onRefresh(id: Long) {
        repo.loadPhotos(albumId = id, isRefreshing = true)
    }

    fun onAddPhoto(photoUri: Uri?, albumId: Long) {
        if (photoUri != null)
            repo.uploadPhoto(
                albumId = albumId,
                uri = photoUri
            )
    }

}