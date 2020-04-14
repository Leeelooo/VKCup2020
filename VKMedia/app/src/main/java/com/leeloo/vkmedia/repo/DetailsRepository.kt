package com.leeloo.vkmedia.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkmedia.requests.GetAlbumPhotosRequest
import com.leeloo.vkmedia.requests.GetTaggedPhotosRequest
import com.leeloo.vkmedia.requests.PostPhotoAlbumsRequest
import com.leeloo.vkmedia.ui.details.DetailsModelState
import com.leeloo.vkmedia.vo.VKPhoto
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class DetailsRepository {
    private val _repoState = MutableLiveData<DetailsModelState>()
    val repoState: LiveData<DetailsModelState>
        get() = _repoState

    init {
        _repoState.value = DetailsModelState.Init
    }

    fun loadPhotos(
        ownerId: Long = 0,
        albumId: Long,
        rev: Int = 1,
        photoSizes: Int = 1,
        offset: Int = 0,
        count: Int = 1000,
        isRefreshing: Boolean = false
    ) {
        _repoState.value =
            if (isRefreshing) DetailsModelState.PhotosRefresherLoading
            else DetailsModelState.PhotosLoading
        if (albumId != -9000L) {
            loadAlbumPhotos(
                ownerId,
                when (albumId) {
                    -6L -> "profile"
                    -7L -> "wall"
                    else -> albumId.toString()
                },
                rev, photoSizes, offset, count, isRefreshing
            )
        } else {
            loadTaggedPhotos(ownerId, rev, count, isRefreshing = isRefreshing)
        }
    }

    fun uploadPhoto(
        uri: Uri,
        albumId: Long,
        groupId: Long = 0L
    ) {
        VK.execute(PostPhotoAlbumsRequest(
            albumId, groupId, uri
        ), object : VKApiCallback<Int> {
            override fun fail(error: Exception) {
                Log.d("PEPEGA", error.message)
            }

            override fun success(result: Int) {
                loadPhotos(albumId = albumId, isRefreshing = true)
            }

        })
    }

    private fun loadAlbumPhotos(
        ownerId: Long,
        albumId: String,
        rev: Int,
        photoSizes: Int,
        offset: Int,
        count: Int,
        isRefreshing: Boolean = false
    ) {
        VK.execute(GetAlbumPhotosRequest(
            ownerId, albumId, rev, photoSizes, offset, count
        ), object : VKApiCallback<List<VKPhoto>> {
            override fun fail(error: Exception) {
                _repoState.postValue(
                    if (isRefreshing) DetailsModelState.PhotosRefreshLoadingFailed(error)
                    else DetailsModelState.PhotosLoadingFailed(error)
                )
            }

            override fun success(result: List<VKPhoto>) {
                _repoState.postValue(DetailsModelState.PhotosLoaded(result))
            }

        })
    }

    private fun loadTaggedPhotos(
        ownerId: Long = 0,
        count: Int,
        rev: Int = 1,
        isRefreshing: Boolean
    ) {
        VK.execute(GetTaggedPhotosRequest(
            ownerId, count, rev
        ), object : VKApiCallback<List<VKPhoto>> {
            override fun fail(error: Exception) {
                _repoState.postValue(
                    if (isRefreshing) DetailsModelState.PhotosRefreshLoadingFailed(error)
                    else DetailsModelState.PhotosLoadingFailed(error)
                )
            }

            override fun success(result: List<VKPhoto>) {
                _repoState.postValue(DetailsModelState.PhotosLoaded(result))
            }

        })
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