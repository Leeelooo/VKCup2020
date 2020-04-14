package com.leeloo.vkdocuments.ui

import com.leeloo.vkdocuments.vo.VKDocument
import com.leeloo.vkdocuments.vo.VKUser
import java.lang.Exception

data class DocumentViewState(
    val user: VKUser?,
    val docs: List<VKDocument>,
    val isUserLoggedIn: Boolean,
    val errorLoadingUser: Exception?,
    val isDocsLoading: Boolean,
    val errorLoadingDocs: Exception?,
    val isRefreshLoading: Boolean,
    val errorRefreshLoading: Exception?
) {
    companion object {
        fun init() = DocumentViewState(
            user = null,
            docs = emptyList(),
            isUserLoggedIn = false,
            errorLoadingUser = null,
            isDocsLoading = false,
            errorLoadingDocs = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun userLogedIn() = DocumentViewState(
            user = null,
            docs = emptyList(),
            isUserLoggedIn = true,
            errorLoadingUser = null,
            isDocsLoading = false,
            errorLoadingDocs = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun userLoaded(
            user: VKUser,
            docs: List<VKDocument>,
            isDocsLoading: Boolean,
            errorLoadingDocs: Exception?
        ) = DocumentViewState(
            user = user,
            docs = docs,
            isUserLoggedIn = true,
            errorLoadingUser = null,
            isDocsLoading = isDocsLoading,
            errorLoadingDocs = errorLoadingDocs,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun userLoadingFailed(
            docs: List<VKDocument>,
            errorLoadingUser: Exception?,
            isDocsLoading: Boolean,
            errorLoadingDocs: Exception?
        ) = DocumentViewState(
            user = null,
            docs = docs,
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = isDocsLoading,
            errorLoadingDocs = errorLoadingDocs,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun docsLoading(
            user: VKUser?,
            errorLoadingUser: Exception?
        ) = DocumentViewState(
            user = user,
            docs = emptyList(),
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = true,
            errorLoadingDocs = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun docsLoaded(
            user: VKUser?,
            docs: List<VKDocument>,
            errorLoadingUser: Exception?
        ) = DocumentViewState(
            user = user,
            docs = docs,
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = false,
            errorLoadingDocs = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun docsLoadingFailed(
            user: VKUser?,
            errorLoadingUser: Exception?,
            errorLoadingDocs: Exception?
        ) = DocumentViewState(
            user = user,
            docs = emptyList(),
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = false,
            errorLoadingDocs = errorLoadingDocs,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun docsRefreshLoading(
            user: VKUser?,
            docs: List<VKDocument>,
            errorLoadingUser: Exception?
        ) = DocumentViewState(
            user = user,
            docs = docs,
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = false,
            errorLoadingDocs = null,
            isRefreshLoading = true,
            errorRefreshLoading = null
        )

        fun docsRefreshLoadingFailed(
            user: VKUser?,
            docs: List<VKDocument>,
            errorLoadingUser: Exception?,
            errorRefreshLoading: Exception?
        ) = DocumentViewState(
            user = user,
            docs = docs,
            isUserLoggedIn = true,
            errorLoadingUser = errorLoadingUser,
            isDocsLoading = false,
            errorLoadingDocs = null,
            isRefreshLoading = false,
            errorRefreshLoading = errorRefreshLoading
        )

    }
}