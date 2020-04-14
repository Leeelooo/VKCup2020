package com.leeloo.vkstore.ui.groups

import com.leeloo.vkstore.vo.VKCity
import com.leeloo.vkstore.vo.VKGroup

data class GroupsViewState(
    val cities: List<VKCity>,
    val groups: List<VKGroup>,
    val queueCity: VKCity?,
    val isBackdropOpen: Boolean,
    val isCitiesLoading: Boolean,
    val errorLoadingCities: Exception?,
    val isGroupLoading: Boolean,
    val errorLoadingGroups: Exception?,
    val isRefreshLoading: Boolean,
    val errorRefreshLoading: Exception?
) {
    companion object {
        fun init() = GroupsViewState(
            cities = emptyList(),
            groups = emptyList(),
            queueCity = null,
            isBackdropOpen = false,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun citiesLoading(isBackdropOpened: Boolean) = GroupsViewState(
            cities = emptyList(),
            groups = emptyList(),
            queueCity = null,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = true,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun citiesLoaded(
            cities: List<VKCity>,
            isBackdropOpened: Boolean
        ) = GroupsViewState(
            cities = cities,
            groups = emptyList(),
            queueCity = null,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun citiesLoadingError(
            error: Exception,
            isBackdropOpened: Boolean
        ) = GroupsViewState(
            cities = emptyList(),
            groups = emptyList(),
            queueCity = null,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = false,
            errorLoadingCities = error,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun cityChoosed(
            cities: List<VKCity>,
            city: VKCity
        ) = GroupsViewState(
            cities = cities,
            groups = emptyList(),
            queueCity = city,
            isBackdropOpen = true,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun groupsLoading(
            cities: List<VKCity>,
            city: VKCity?
        ) = GroupsViewState(
            cities = cities,
            groups = emptyList(),
            queueCity = city,
            isBackdropOpen = false,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = true,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun groupsLoaded(
            cities: List<VKCity>,
            groups: List<VKGroup>,
            city: VKCity?,
            isBackdropOpened: Boolean
        ) = GroupsViewState(
            cities = cities,
            groups = groups,
            queueCity = city,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun groupsLoadingError(
            cities: List<VKCity>,
            city: VKCity?,
            error: Exception,
            isBackdropOpened: Boolean
        ) = GroupsViewState(
            cities = cities,
            groups = emptyList(),
            queueCity = city,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = error,
            isRefreshLoading = false,
            errorRefreshLoading = null
        )

        fun refreshLoading(
            cities: List<VKCity>,
            groups: List<VKGroup>,
            city: VKCity?
        ) = GroupsViewState(
            cities = cities,
            groups = groups,
            queueCity = city,
            isBackdropOpen = false,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = true,
            errorRefreshLoading = null
        )

        fun refreshLoadingError(
            cities: List<VKCity>,
            groups: List<VKGroup>,
            city: VKCity?,
            isBackdropOpened: Boolean,
            error: Exception
        ) = GroupsViewState(
            cities = cities,
            groups = groups,
            queueCity = city,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = false,
            errorLoadingCities = null,
            isGroupLoading = false,
            errorLoadingGroups = null,
            isRefreshLoading = false,
            errorRefreshLoading = error
        )

        fun backdropedChanged(
            cities: List<VKCity>,
            groups: List<VKGroup>,
            city: VKCity?,
            isBackdropOpened: Boolean,
            isCitiesLoading: Boolean,
            errorLoadingCities: Exception?,
            isGroupLoading: Boolean,
            errorLoadingGroups: Exception?,
            isRefreshLoading: Boolean,
            errorRefreshLoading: Exception?
        ) = GroupsViewState(
            cities = cities,
            groups = groups,
            queueCity = city,
            isBackdropOpen = isBackdropOpened,
            isCitiesLoading = isCitiesLoading,
            errorLoadingCities = errorLoadingCities,
            isGroupLoading = isGroupLoading,
            errorLoadingGroups = errorLoadingGroups,
            isRefreshLoading = isRefreshLoading,
            errorRefreshLoading = errorRefreshLoading
        )

    }
}