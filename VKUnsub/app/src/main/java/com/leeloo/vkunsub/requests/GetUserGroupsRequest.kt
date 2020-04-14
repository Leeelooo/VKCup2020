package com.leeloo.vkunsub.requests

import com.leeloo.vkunsub.vo.VKGroup
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetUserGroupsRequest(
    userId: Long,
    extended: Int,
    fields: Array<String>,
    offset: Int,
    count: Int
) : VKRequest<List<VKGroup>>("groups.get") {

    init {
        if (userId != 0L)
            addParam("userId", userId)
        addParam("extended", extended)
        addParam("fields", fields.joinToString(separator = ","))
        addParam("offset", offset)
        addParam("count", count)
    }

    override fun parse(r: JSONObject) = VKGroup.parse(r)

}