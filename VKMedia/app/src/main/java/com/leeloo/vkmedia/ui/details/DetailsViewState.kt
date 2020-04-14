package com.leeloo.vkmedia.ui.details

import com.leeloo.vkmedia.vo.VKPhoto
import java.lang.Exception

data class DetailsViewState(
    val photos: List<VKPhoto>,
    val isPhotosLoading: Boolean,
    val errorLoadingPhotos: Exception?,
    val isRefreshLoading: Boolean,
    val errorRefreshLoading: Exception?
) {
    companion object {
        fun init() = DetailsViewState(
            photos = emptyList(),
            isPhotosLoading = false,
            errorLoadingPhotos = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun photosLoading() = DetailsViewState(
            photos = emptyList(),
            isPhotosLoading = true,
            errorLoadingPhotos = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun photosLoaded(
            photos: List<VKPhoto>
        ) = DetailsViewState(
            photos = photos,
            isPhotosLoading = false,
            errorLoadingPhotos = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun photosLoadingFailed(
            errorLoadingPhotos: Exception?
        ) = DetailsViewState(
            photos = emptyList(),
            isPhotosLoading = false,
            errorLoadingPhotos = errorLoadingPhotos,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun photosRefreshLoading(
            photos: List<VKPhoto>
        ) = DetailsViewState(
            photos = photos,
            isPhotosLoading = false,
            errorLoadingPhotos = null,
            isRefreshLoading = true,
            errorRefreshLoading = null
        )

        fun photosRefreshLoadingFailed(
            errorRefreshLoading: Exception?
        ) = DetailsViewState(
            photos = emptyList(),
            isPhotosLoading = false,
            errorLoadingPhotos = null,
            isRefreshLoading = false,
            errorRefreshLoading = errorRefreshLoading
        )

    }
}