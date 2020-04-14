package com.leeloo.vkdocuments.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkdocuments.requests.DeleteDocumentRequest
import com.leeloo.vkdocuments.requests.EditDocumentRequest
import com.leeloo.vkdocuments.requests.GetDocumentRequest
import com.leeloo.vkdocuments.ui.DocumentModelState
import com.leeloo.vkdocuments.vo.VKDocument
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import timber.log.Timber

class DocumentRepository {
    private val _liveData = MutableLiveData<DocumentModelState>()
    val liveData: LiveData<DocumentModelState>
        get() = _liveData

    init {
        clear()
    }

    fun requestDocs(
        count: Int = 0,
        offset: Int = 0,
        type: Int = 0,
        returnTags: Int = 1,
        isRefresher: Boolean = false
    ) {
        _liveData.value =
            if (isRefresher) DocumentModelState.DocsRefresherLoading
            else DocumentModelState.DocsLoading
        VK.execute(
            GetDocumentRequest(
                count = count,
                offset = offset,
                type = type,
                returnTags = returnTags
            ), object : VKApiCallback<List<VKDocument>> {
                override fun fail(error: Exception) {
                    if (VK.isLoggedIn())
                        _liveData.postValue(
                            if (isRefresher)
                                DocumentModelState.DocsRefreshLoadingFailed(error)
                            else
                                DocumentModelState.DocsLoadingFailed(error)
                        )
                }

                override fun success(result: List<VKDocument>) {
                    _liveData.postValue(DocumentModelState.DocsLoaded(result))
                }
            })
    }

    fun renameDoc(
        ownerId: Long = 0L,
        docId: Long,
        newName: String
    ) {
        VK.execute(
            EditDocumentRequest(
                ownerId = ownerId,
                documentId = docId,
                newName = newName
            ), object : VKApiCallback<Boolean> {
                override fun fail(error: Exception) {

                }

                override fun success(result: Boolean) {
                    if (result)
                        requestDocs(isRefresher = true)

                }

            }
        )
    }

    fun deleteDoc(
        ownerId: Long = 0L,
        docId: Long
    ) {
        VK.execute(
            DeleteDocumentRequest(
                ownerId = ownerId,
                documentId = docId
            ), object : VKApiCallback<Boolean> {
                override fun fail(error: Exception) {

                }

                override fun success(result: Boolean) {
                    if (result)
                        requestDocs(isRefresher = true)

                }

            }
        )
    }

    fun clear() {
        _liveData.value = DocumentModelState.Init
    }

    companion object {
        private lateinit var _instance: DocumentRepository
        fun getInstance(): DocumentRepository {
            if (!::_instance.isInitialized)
                _instance = DocumentRepository()
            return _instance
        }
    }
}