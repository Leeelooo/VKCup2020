package com.leeloo.vkmedia.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkmedia.requests.CreateAlbumRequest
import com.leeloo.vkmedia.requests.DeleteAlbumRequest
import com.leeloo.vkmedia.requests.GetAlbumsRequest
import com.leeloo.vkmedia.ui.albums.AlbumsModelState
import com.leeloo.vkmedia.vo.VKAlbum
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class AlbumsRepository {
    private val _repoState = MutableLiveData<AlbumsModelState>()
    val repoState: LiveData<AlbumsModelState>
        get() = _repoState

    init {
        _repoState.value = AlbumsModelState.Init
    }

    fun loadAlbums(
        ownerId: Long = 0,
        albumsId: IntArray = intArrayOf(),
        offset: Int = 0,
        count: Int = 0,
        needSystem: Int = 1,
        needCovers: Int = 1,
        photoSizes: Int = 1,
        isRefreshing: Boolean = false
    ) {
        _repoState.value =
            if (isRefreshing) AlbumsModelState.AlbumsRefresherLoading
            else AlbumsModelState.AlbumsLoading
        VK.execute(GetAlbumsRequest(
            ownerId, albumsId, offset, count, needSystem, needCovers, photoSizes
        ), object : VKApiCallback<List<VKAlbum>> {
            override fun fail(error: Exception) {
                _repoState.postValue(
                    if (isRefreshing) AlbumsModelState.AlbumsRefreshLoadingFailed(error)
                    else AlbumsModelState.AlbumsLoadingFailed(error)
                )
            }

            override fun success(result: List<VKAlbum>) {
                _repoState.postValue(AlbumsModelState.AlbumsLoaded(result))
            }

        })
    }

    fun changeEditMode(editMode: Boolean) {
        _repoState.value = AlbumsModelState.EditModeChange(editMode)
    }

    fun createAlbum(
        title: String,
        privacyView: String = "",
        privacyComment: String = ""
    ) {
        VK.execute(CreateAlbumRequest(title, privacyView, privacyComment), object : VKApiCallback<Boolean> {
            override fun fail(error: Exception) { }
            override fun success(result: Boolean) {
                if (result)
                    loadAlbums(isRefreshing = true)
            }
        })
    }

    fun deleteAlbums(
        albumId: Long,
        groupId: Long = 0L
    ) {
        VK.execute(DeleteAlbumRequest(albumId, groupId), object : VKApiCallback<Boolean> {
            override fun fail(error: Exception) { }
            override fun success(result: Boolean) {
                if (result)
                    loadAlbums(isRefreshing = true)
            }
        })
    }

    companion object {
        private lateinit var _instance: AlbumsRepository
        fun getInstance(): AlbumsRepository {
            if (!::_instance.isInitialized)
                _instance = AlbumsRepository()
            return _instance
        }
    }

}