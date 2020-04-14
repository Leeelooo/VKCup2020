package com.leeloo.vkunsub.ui

import com.leeloo.vkunsub.vo.VKGroup

sealed class GroupModelState {
    abstract fun reduce(oldState: GroupViewState): GroupViewState

    object Init : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.init()
    }

    object GroupsLoading : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.loadingGroups()
    }

    class GroupsLoaded(
        private val groups: List<VKGroup>
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState
            .groupsLoaded(groups, oldState.selected)
    }

    class GroupsLoadingError(
        private val error: Exception
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.groupsLoadingError(error)
    }

    class GroupsSelectingChanged(
        private val selected: HashSet<VKGroup>
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState
            .selectedGroupsChanged(oldState.groups, selected)
    }

    object RefreshLoading : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.refreshLoading(
            oldState.groups,
            oldState.selected,
            oldState.groupInfo,
            oldState.isGroupDetailsLoading,
            oldState.groupDetailsLoadingError
        )
    }

    class RefreshLoadingError(
        private val error: Exception
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.refreshLoadingError(
            oldState.groups,
            oldState.selected,
            oldState.groupInfo,
            oldState.isGroupDetailsLoading,
            oldState.groupDetailsLoadingError,
            error
        )
    }

    class GroupDetailsChoosed(
        private val group: VKGroup
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.groupDetailsChoosed(
            oldState.groups,
            oldState.selected,
            group,
            oldState.isRefreshLoading,
            oldState.refreshLoadingError
        )
    }

    object GroupDetailsLoading : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.loadingGroupDetails(
            oldState.groups,
            oldState.selected,
            oldState.groupInfo!!,
            oldState.isRefreshLoading,
            oldState.refreshLoadingError
        )
    }

    class GroupDetailsLoaded(
        private val group: VKGroup
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.groupDetailsChoosed(
            oldState.groups,
            oldState.selected,
            group,
            oldState.isRefreshLoading,
            oldState.refreshLoadingError
        )
    }

    class GroupDetailsLoadingError(
        private val error: Exception
    ) : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState.loadingGroupDetailsError(
            oldState.groups,
            oldState.selected,
            oldState.groupInfo!!,
            oldState.isRefreshLoading,
            oldState.refreshLoadingError,
            error
        )
    }

    object ClearSelection : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState(
            groups = oldState.groups,
            selected = hashSetOf(),
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = oldState.isRefreshLoading,
            refreshLoadingError = oldState.refreshLoadingError,
            isGroupDetailsLoading = oldState.isGroupDetailsLoading,
            groupDetailsLoadingError = oldState.groupDetailsLoadingError

        )
    }

    object Dismiss : GroupModelState() {
        override fun reduce(oldState: GroupViewState) = GroupViewState(
            groups = oldState.groups,
            selected = oldState.selected,
            groupInfo = null,
            isLoggedIn = true,
            isGroupsLoading = false,
            groupLoadingError = null,
            isRefreshLoading = oldState.isRefreshLoading,
            refreshLoadingError = oldState.refreshLoadingError,
            isGroupDetailsLoading = false,
            groupDetailsLoadingError = null

        )
    }

}