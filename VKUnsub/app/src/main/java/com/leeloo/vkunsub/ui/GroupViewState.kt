package com.leeloo.vkunsub.ui

import com.leeloo.vkunsub.vo.VKGroup

data class GroupViewState(
    val groups: List<VKGroup>,
    val selected: HashSet<VKGroup>,
    val groupInfo: VKGroup?,
    val isLoggedIn: Boolean,
    val isGroupsLoading: Boolean,
    val groupLoadingError: Exception?,
    val isRefreshLoading: Boolean,
    val refreshLoadingError: Exception?,
    val isGroupDetailsLoading: Boolean,
    val groupDetailsLoadingError: Exception?
) {
    companion object {

        fun init() = GroupViewState(
            groups = emptyList(),
            selected = hashSetOf(),
            groupInfo = null,
            isLoggedIn = false,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = false,
            refreshLoadingError = null,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun loadingGroups() = GroupViewState(
            groups = emptyList(),
            selected = hashSetOf(),
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = true,
            groupLoadingError = null,
            isRefreshLoading = false,
            refreshLoadingError = null,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun groupsLoaded(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = false,
            refreshLoadingError = null,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun groupsLoadingError(error: Exception) = GroupViewState(
            groups = emptyList(),
            selected = hashSetOf(),
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = error,
            isRefreshLoading = false,
            refreshLoadingError = null,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun selectedGroupsChanged(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = false,
            refreshLoadingError = null,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun refreshLoading(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>,
            groupInfo: VKGroup?,
            isGroupDetailsLoading: Boolean,
            groupDetailsLoadingError: Exception?
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = groupInfo,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = true,
            refreshLoadingError = null,
            isGroupDetailsLoading = isGroupDetailsLoading,
            groupDetailsLoadingError = groupDetailsLoadingError
        )

        fun refreshLoadingError(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>,
            groupInfo: VKGroup?,
            isGroupDetailsLoading: Boolean,
            groupDetailsLoadingError: Exception?,
            error: Exception
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = groupInfo,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = false,
            refreshLoadingError = error,
            isGroupDetailsLoading = isGroupDetailsLoading,
            groupDetailsLoadingError = groupDetailsLoadingError
        )

        fun groupDetailsChoosed(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>,
            group: VKGroup,
            isRefreshLoading: Boolean,
            refreshLoadingError: Exception?
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = group,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = isRefreshLoading,
            refreshLoadingError = refreshLoadingError,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null
        )

        fun loadingGroupDetails(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>,
            group: VKGroup,
            isRefreshLoading: Boolean,
            refreshLoadingError: Exception?
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = group,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = isRefreshLoading,
            refreshLoadingError = refreshLoadingError,
            isGroupDetailsLoading = true,
            groupDetailsLoadingError = null
        )

        fun loadingGroupDetailsError(
            groups: List<VKGroup>,
            selected: HashSet<VKGroup>,
            group: VKGroup,
            isRefreshLoading: Boolean,
            refreshLoadingError: Exception?,
            error: Exception?
        ) = GroupViewState(
            groups = groups,
            selected = selected,
            groupInfo = group,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = isRefreshLoading,
            refreshLoadingError = refreshLoadingError,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = error
        )

    }
}