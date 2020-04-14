package com.leeloo.vkunsub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkunsub.repo.GroupsRepository
import com.leeloo.vkunsub.ui.GroupViewState
import com.leeloo.vkunsub.vo.VKGroup

class GroupViewModel : ViewModel() {
    private val repo = GroupsRepository.getInstance()
    private val _viewState = MediatorLiveData<GroupViewState>()
    val viewState: LiveData<GroupViewState>
        get() = _viewState

    init {
        _viewState.value = GroupViewState.init()
        _viewState.addSource(repo.modelState) {
            _viewState.value = it.reduce(_viewState.value!!)
        }
    }

    fun onLogin() {
        repo.loadGroups(isRefreshing = false)
    }

    fun onLogout() {
        repo.logout()
    }

    fun onRetry() {
        repo.loadGroups(isRefreshing = false)
    }

    fun onRefresh() {
        repo.loadGroups(isRefreshing = true)
    }

    fun onDismiss() {
        repo.dismiss()
    }

    fun onClick(group: VKGroup) {
        repo.changeSelectGroup(_viewState.value!!.selected, group)
    }

    fun onLongClick(group: VKGroup): Boolean {
        repo.loadGroupDetails(group)
        return true
    }

    fun onUnsubscribeClick() {
        repo.unsubGroups(
            _viewState.value!!.selected.map { it.id.toInt() }.toIntArray()
        )
    }

}