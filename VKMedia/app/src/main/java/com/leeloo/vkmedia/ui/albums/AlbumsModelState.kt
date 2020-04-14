package com.leeloo.vkmedia.ui.albums

import com.leeloo.vkmedia.vo.VKAlbum
import java.lang.Exception

sealed class AlbumsModelState {
    abstract fun reduce(oldState: AlbumsViewState): AlbumsViewState

    object Init : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) =
            AlbumsViewState.init()
    }

    object AlbumsLoading : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) =
            AlbumsViewState.albumsLoading()
    }

    class AlbumsLoaded(
        private val albums: List<VKAlbum>
    ) : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) =
            AlbumsViewState.albumsLoaded(albums = albums, editMode = oldState.isEditMode)
    }

    class AlbumsLoadingFailed(
        private val error: Exception
    ) : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) =
            AlbumsViewState.albumsLoadingFailed(errorLoadingAlbums = error)
    }

    class EditModeChange(
        private val editMode: Boolean
    ) : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) =
            AlbumsViewState.changeEditMode(
                oldState.albums,
                oldState.isAlbumLoading,
                oldState.errorLoadingAlbums,
                oldState.isRefreshLoading,
                oldState.errorRefreshLoading,
                editMode
            )
    }

    object AlbumsRefresherLoading : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) = AlbumsViewState
            .albumsRefreshLoading(albums = oldState.albums, editMode = oldState.isEditMode)
    }

    class AlbumsRefreshLoadingFailed(
        private val error: Exception
    ) : AlbumsModelState() {
        override fun reduce(oldState: AlbumsViewState) = AlbumsViewState
            .albumsRefreshLoadingFailed(errorRefreshLoading = error, editMode = oldState.isEditMode)
    }

}