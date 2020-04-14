package com.leeloo.vkstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.leeloo.vkstore.repo.GroupsRepository
import com.leeloo.vkstore.ui.groups.GroupsViewState
import com.leeloo.vkstore.vo.VKCity

class GroupsViewModel : ViewModel() {
    private val repo = GroupsRepository.getInstance()
    private val _viewState = MediatorLiveData<GroupsViewState>()
    val viewState: LiveData<GroupsViewState>
        get() = _viewState

    init {
        _viewState.value = GroupsViewState.init()
        _viewState.addSource(repo.modelState) {
            _viewState.value = it.reduce(_viewState.value!!)
        }
        repo.loadCities()
    }

    fun onCitiesRetry() {
        repo.loadCities()
    }

    fun onCityChoosed(city: VKCity) {
        repo.cityChoosed(city)
        repo.loadGroupsAtCity(cityId = city.id)
    }

    fun onGroupsRetry() {
        repo.loadGroupsAtCity(
            cityId = _viewState.value!!.queueCity!!.id
        )
    }

    fun onDropdownClicked() {
        repo.changeDropdownState(true)
    }

    fun onExitDropdownClicked() {
        repo.changeDropdownState(false)
    }

    fun onRefresh() {
        repo.loadGroupsAtCity(
            cityId = _viewState.value!!.queueCity!!.id,
            isRefresh = true
        )
    }

}