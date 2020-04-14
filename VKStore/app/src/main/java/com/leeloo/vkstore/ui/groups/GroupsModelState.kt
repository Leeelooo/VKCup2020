package com.leeloo.vkstore.ui.groups

import com.leeloo.vkstore.vo.VKCity
import com.leeloo.vkstore.vo.VKGroup

sealed class GroupsModelState {
    abstract fun reduce(oldState: GroupsViewState): GroupsViewState

    object Init : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.init()
    }

    object CitiesLoading : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.citiesLoading(oldState.isBackdropOpen)
    }

    class CitiesLoaded(
        private val cities: List<VKCity>
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.citiesLoaded(cities, oldState.isBackdropOpen)
    }

    class CitiesLoadingError(
        private val error: Exception
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.citiesLoadingError(error, oldState.isBackdropOpen)
    }

    class CityChoosed(
        private val city: VKCity
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.cityChoosed(oldState.cities, city)
    }

    object GroupLoading : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.groupsLoading(oldState.cities, oldState.queueCity)
    }

    class GroupsLoaded(
        private val groups: List<VKGroup>
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.groupsLoaded(
                oldState.cities,
                groups,
                oldState.queueCity,
                oldState.isBackdropOpen
            )
    }

    class GroupsLoadingError(
        private val error: Exception
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.groupsLoadingError(
                oldState.cities,
                oldState.queueCity,
                error,
                oldState.isBackdropOpen
            )
    }

    object RefreshLoading : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.refreshLoading(oldState.cities, oldState.groups, oldState.queueCity)
    }

    class RefreshLoadingError(
        private val error: Exception
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) = GroupsViewState.refreshLoadingError(
            oldState.cities, oldState.groups, oldState.queueCity, oldState.isBackdropOpen, error
        )
    }

    class BackdropChanged(
        private val isBackdropOpened: Boolean
    ) : GroupsModelState() {
        override fun reduce(oldState: GroupsViewState) =
            GroupsViewState.backdropedChanged(
                oldState.cities,
                oldState.groups,
                oldState.queueCity,
                isBackdropOpened,
                oldState.isCitiesLoading,
                oldState.errorLoadingCities,
                oldState.isGroupLoading,
                oldState.errorLoadingGroups,
                oldState.isRefreshLoading,
                oldState.errorRefreshLoading
            )
    }

}