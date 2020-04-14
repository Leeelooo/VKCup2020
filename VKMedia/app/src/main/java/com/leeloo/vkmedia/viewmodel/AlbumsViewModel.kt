package com.leeloo.vkmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkmedia.repo.AlbumsRepository
import com.leeloo.vkmedia.ui.albums.AlbumsViewState

class AlbumsViewModel : ViewModel() {
    private val repo = AlbumsRepository.getInstance()

    private val _viewStateData = MediatorLiveData<AlbumsViewState>()
    val viewStateData: LiveData<AlbumsViewState>
        get() = _viewStateData

    init {
        _viewStateData.value = AlbumsViewState.init()
        _viewStateData.addSource(repo.repoState) {
            _viewStateData.value = it.reduce(_viewStateData.value!!)
        }
        repo.loadAlbums()
    }

    fun onRetry() {
        repo.loadAlbums()
    }

    fun onRefresh() {
        repo.loadAlbums(isRefreshing = true)
    }

    fun onCreateAlbum(title: String) {
        repo.createAlbum(title)
    }

    fun onEnterEditMode() {
        repo.changeEditMode(true)
    }

    fun onExitEditMode() {
        repo.changeEditMode(false)
    }

    fun onLongTap(): Boolean {
        repo.changeEditMode(true)
        return true
    }

    fun onDeleteAlbum(albumId: Long) {
        repo.deleteAlbums(albumId)
    }

}