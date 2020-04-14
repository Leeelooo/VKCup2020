package com.leeloo.vkunsub.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class LeaveGroupRequest(
    id: Int
) : VKRequest<Boolean>("groups.leave") {

    init {
        addParam("group_id", id)
    }

    override fun parse(r: JSONObject) = r.optInt("response") == 1
}