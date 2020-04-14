package com.leeloo.vkdocuments.ui

import com.leeloo.vkdocuments.vo.VKDocument
import com.leeloo.vkdocuments.vo.VKUser
import java.lang.Exception

sealed class DocumentModelState {
    abstract fun reduce(oldState: DocumentViewState): DocumentViewState

    object Init : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState.init()
    }

    object UserLoggedIn : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState.userLogedIn()
    }

    class UserLoaded(
        private val user: VKUser
    ) : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState
            .userLoaded(
                user = user,
                docs = oldState.docs,
                isDocsLoading = oldState.isDocsLoading,
                errorLoadingDocs = oldState.errorLoadingDocs
            )
    }

    class UserLoadingFailed(
        private val error: Exception
    ) : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState
            .userLoadingFailed(
                docs = oldState.docs,
                errorLoadingUser = error,
                isDocsLoading = oldState.isDocsLoading,
                errorLoadingDocs = oldState.errorLoadingDocs
            )
    }

    object DocsLoading : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState.docsLoading(
                user = oldState.user,
                errorLoadingUser = oldState.errorLoadingUser
            )
    }

    class DocsLoaded(
        private val docs: List<VKDocument>
    ) : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState
            .docsLoaded(
                user = oldState.user,
                docs = docs,
                errorLoadingUser = oldState.errorLoadingUser
            )
    }

    class DocsLoadingFailed(
        private val error: Exception
    ) : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState
            .docsLoadingFailed(
                user = oldState.user,
                errorLoadingUser = oldState.errorLoadingUser,
                errorLoadingDocs = error
            )
    }

    object DocsRefresherLoading : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState.docsRefreshLoading(
            user = oldState.user,
            docs = oldState.docs,
            errorLoadingUser = oldState.errorLoadingUser
        )
    }

    class DocsRefreshLoadingFailed(
        private val error: Exception
    ) : DocumentModelState() {
        override fun reduce(oldState: DocumentViewState) = DocumentViewState
            .docsRefreshLoadingFailed(
                user = oldState.user,
                docs = oldState.docs,
                errorLoadingUser = oldState.errorLoadingUser,
                errorRefreshLoading = error
            )
    }

}