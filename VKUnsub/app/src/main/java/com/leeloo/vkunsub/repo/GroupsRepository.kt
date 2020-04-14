package com.leeloo.vkunsub.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leeloo.vkunsub.requests.GetGroupPostsRequest
import com.leeloo.vkunsub.requests.GetUserGroupsRequest
import com.leeloo.vkunsub.requests.LeaveGroupRequest
import com.leeloo.vkunsub.ui.GroupModelState
import com.leeloo.vkunsub.vo.VKGroup
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import java.util.concurrent.atomic.AtomicInteger

class GroupsRepository {
    private val _modelState = MutableLiveData<GroupModelState>()
    val modelState: LiveData<GroupModelState>
        get() = _modelState

    init {
        if (_modelState.value == null)
            _modelState.value = GroupModelState.Init
    }

    fun logout() {
        _modelState.value = GroupModelState.Init
    }

    fun dismiss() {
        _modelState.value = GroupModelState.Dismiss
    }

    fun changeSelectGroup(selected: HashSet<VKGroup>, group: VKGroup) {
        val list = hashSetOf<VKGroup>()
        list.addAll(selected)
        if (list.contains(group))
            list.remove(group)
        else
            list.add(group)
        _modelState.value = GroupModelState.GroupsSelectingChanged(list)
    }

    fun loadGroupDetails(group: VKGroup) {
        _modelState.value = GroupModelState.GroupDetailsChoosed(group)
        _modelState.value = GroupModelState.GroupDetailsLoading
        VK.execute(GetGroupPostsRequest(group.id * -1), object : VKApiCallback<Long> {
            override fun fail(error: Exception) {
                _modelState.value = GroupModelState.GroupDetailsLoadingError(error)
            }

            override fun success(result: Long) {
                group.lastTimestamp = result
                _modelState.value = GroupModelState.GroupDetailsLoaded(group)
            }

        })
    }

    fun loadGroups(
        userId: Long = 0L,
        extended: Int = 1,
        fields: Array<String> = arrayOf("description", "members_count"),
        offset: Int = 0,
        count: Int = 1000,
        isRefreshing: Boolean = false
    ) {
        _modelState.value =
            if (isRefreshing) GroupModelState.RefreshLoading
            else GroupModelState.GroupsLoading
        VK.execute(
            GetUserGroupsRequest(
                userId, extended, fields, offset, count
            ), object : VKApiCallback<List<VKGroup>> {
                override fun fail(error: Exception) {
                    _modelState.value =
                        if (isRefreshing) GroupModelState.RefreshLoadingError(error)
                        else GroupModelState.GroupsLoadingError(error)
                }

                override fun success(result: List<VKGroup>) {
                    _modelState.value = GroupModelState.GroupsLoaded(result.reversed())
                }
            }
        )
    }

    fun unsubGroups(ids: IntArray) {
        _modelState.value = GroupModelState.ClearSelection
        val requestPerformed = AtomicInteger()
        ids.forEach {
            VK.execute(LeaveGroupRequest(it), object : VKApiCallback<Boolean> {
                override fun fail(error: Exception) {
                    if (requestPerformed.incrementAndGet() == ids.size)
                        loadGroups(isRefreshing = true)
                }

                override fun success(result: Boolean) {
                    if (requestPerformed.incrementAndGet() == ids.size)
                        loadGroups(isRefreshing = true)
                }

            })
        }
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