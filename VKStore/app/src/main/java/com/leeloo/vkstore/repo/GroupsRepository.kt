package com.leeloo.vkstore.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkstore.requests.GetCitiesRequest
import com.leeloo.vkstore.requests.GetGroupsSearchRequest
import com.leeloo.vkstore.ui.groups.GroupsModelState
import com.leeloo.vkstore.vo.VKCity
import com.leeloo.vkstore.vo.VKGroup
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class GroupsRepository {
    private val _modelState = MutableLiveData<GroupsModelState>()
    val modelState: LiveData<GroupsModelState>
        get() = _modelState

    init {
        _modelState.value = GroupsModelState.Init
    }

    fun loadCities(
        countryId: Long = 1L,
        needAll: Int = 0
    ) {
        _modelState.value = GroupsModelState.CitiesLoading
        VK.execute(
            GetCitiesRequest(countryId, needAll),
            object : VKApiCallback<List<VKCity>> {
                override fun fail(error: Exception) {
                    _modelState.postValue(GroupsModelState.CitiesLoadingError(error))
                }

                override fun success(result: List<VKCity>) {
                    _modelState.postValue(GroupsModelState.CitiesLoaded(result))
                }
            }
        )
    }

    fun loadGroupsAtCity(
        queue: String = "*",
        cityId: Long,
        count: Int = 1000,
        market: Int = 1,
        sort: Int = 1,
        isRefresh: Boolean = false
    ) {
        _modelState.value =
            if (isRefresh) GroupsModelState.RefreshLoading
            else GroupsModelState.GroupLoading
        VK.execute(
            GetGroupsSearchRequest(queue, cityId, count, market, sort),
            object : VKApiCallback<List<VKGroup>> {
                override fun fail(error: Exception) {
                    _modelState.postValue(
                        if (isRefresh) GroupsModelState.RefreshLoadingError(error)
                        else GroupsModelState.GroupsLoadingError(error)
                    )
                }

                override fun success(result: List<VKGroup>) {
                    _modelState.postValue(GroupsModelState.GroupsLoaded(result))
                }
            }
        )
    }

    fun cityChoosed(city: VKCity) {
        _modelState.value = GroupsModelState.CityChoosed(city)
    }

    fun changeDropdownState(isOpen: Boolean) {
        _modelState.value = GroupsModelState.BackdropChanged(isOpen)
    }

    companion object {
        private lateinit var _instance: GroupsRepository
        fun getInstance(): GroupsRepository {
            if (!::_instance.isInitialized)
                _instance = GroupsRepository()
            return _instance
        }
    }
}