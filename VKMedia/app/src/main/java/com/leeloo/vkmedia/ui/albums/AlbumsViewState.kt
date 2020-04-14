package com.leeloo.vkmedia.ui.albums

import com.leeloo.vkmedia.vo.VKAlbum
import java.lang.Exception

data class AlbumsViewState(
    val albums: List<VKAlbum>,
    val isAlbumLoading: Boolean,
    val errorLoadingAlbums: Exception?,
    val isRefreshLoading: Boolean,
    val errorRefreshLoading: Exception?,
    val isEditMode: Boolean
) {
    companion object {
        fun init() = AlbumsViewState(
            albums = emptyList(),
            isAlbumLoading = false,
            errorLoadingAlbums = null,
            isRefreshLoading = false,
            errorRefreshLoading = null,
            isEditMode = false
        )

        fun albumsLoading() = AlbumsViewState(
            albums = emptyList(),
            isAlbumLoading = true,
            errorLoadingAlbums = null,
            isRefreshLoading = false,
            errorRefreshLoading = null,
            isEditMode = false
        )

        fun albumsLoaded(
            albums: List<VKAlbum>,
            editMode: Boolean
        ) = AlbumsViewState(
            albums = albums,
            isAlbumLoading = false,
            errorLoadingAlbums = null,
            isRefreshLoading = false,
            errorRefreshLoading = null,
            isEditMode = editMode
        )

        fun albumsLoadingFailed(
            errorLoadingAlbums: Exception?
        ) = AlbumsViewState(
            albums = emptyList(),
            isAlbumLoading = false,
            errorLoadingAlbums = errorLoadingAlbums,
            isRefreshLoading = false,
            errorRefreshLoading = null,
            isEditMode = false
        )

        fun changeEditMode(
            albums: List<VKAlbum>,
            isAlbumLoading: Boolean,
            errorLoadingAlbums: Exception?,
            isRefreshLoading: Boolean,
            errorRefreshLoading: Exception?,
            editMode: Boolean
        ) = AlbumsViewState(
            albums = albums,
            isAlbumLoading = isAlbumLoading,
            errorLoadingAlbums = errorLoadingAlbums,
            isRefreshLoading = isRefreshLoading,
            errorRefreshLoading = errorRefreshLoading,
            isEditMode = editMode
        )

        fun albumsRefreshLoading(
            albums: List<VKAlbum>,
            editMode: Boolean
        ) = AlbumsViewState(
            albums = albums,
            isAlbumLoading = false,
            errorLoadingAlbums = null,
            isRefreshLoading = true,
            errorRefreshLoading = null,
            isEditMode = editMode
        )

        fun albumsRefreshLoadingFailed(
            errorRefreshLoading: Exception?,
            editMode: Boolean
        ) = AlbumsViewState(
            albums = emptyList(),
            isAlbumLoading = false,
            errorLoadingAlbums = null,
            isRefreshLoading = false,
            errorRefreshLoading = errorRefreshLoading,
            isEditMode = editMode
        )

    }
}