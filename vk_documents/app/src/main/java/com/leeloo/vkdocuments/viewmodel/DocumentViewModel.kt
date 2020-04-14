package com.leeloo.vkdocuments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkdocuments.repo.DocumentRepository
import com.leeloo.vkdocuments.repo.UserRepository
import com.leeloo.vkdocuments.ui.DocumentViewState
import com.leeloo.vkdocuments.vo.VKDocument
import timber.log.Timber

class DocumentViewModel : ViewModel() {
    private val userRepo = UserRepository.getInstance()
    private val docRepo = DocumentRepository.getInstance()

    private val _viewStateData = MediatorLiveData<DocumentViewState>()
    val viewStateData: LiveData<DocumentViewState>
        get() = _viewStateData

    init {
        _viewStateData.value = DocumentViewState.init()
        _viewStateData.addSource(userRepo.liveData) {
            _viewStateData.value = it.reduce(_viewStateData.value!!)
            Timber.d(_viewStateData.value.toString())
            Timber.d(it.toString())
        }
        _viewStateData.addSource(docRepo.liveData) {
            _viewStateData.value = it.reduce(_viewStateData.value!!)
            Timber.d(_viewStateData.value.toString())
            Timber.d(it.toString())
        }
    }

    fun onUserLoggedIn() {
        userRepo.userLoggedIn()
        docRepo.requestDocs()
    }

    fun onRefresh() {
        docRepo.requestDocs(isRefresher = true)
    }

    fun onDocumentRename(document: VKDocument, newName: String) {
        docRepo.renameDoc(ownerId = document.ownerId, docId = document.id, newName = newName)
    }

    fun onDocumentDelete(document: VKDocument) {
        docRepo.deleteDoc(ownerId = document.ownerId, docId = document.id)
    }

    fun onUserLoggedOut() {
        userRepo.clear()
        docRepo.clear()
    }

}