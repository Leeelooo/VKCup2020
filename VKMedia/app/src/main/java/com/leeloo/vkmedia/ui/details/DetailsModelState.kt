package com.leeloo.vkmedia.ui.details

import com.leeloo.vkmedia.vo.VKPhoto

sealed class DetailsModelState {
    abstract fun reduce(oldState: DetailsViewState): DetailsViewState

    object Init : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.init()
    }

    object PhotosLoading : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.photosLoading()
    }

    class PhotosLoaded(
        private val photos: List<VKPhoto>
    ) : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.photosLoaded(photos = photos)
    }

    class PhotosLoadingFailed(
        private val error: Exception
    ) : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.photosLoadingFailed(errorLoadingPhotos = error)
    }

    object PhotosRefresherLoading : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.photosRefreshLoading(photos = oldState.photos)
    }

    class PhotosRefreshLoadingFailed(
        private val error: Exception
    ) : DetailsModelState() {
        override fun reduce(oldState: DetailsViewState) =
            DetailsViewState.photosRefreshLoadingFailed(errorRefreshLoading = error)
    }

}