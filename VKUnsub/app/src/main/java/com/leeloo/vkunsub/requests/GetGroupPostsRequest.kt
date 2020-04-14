package com.leeloo.vkunsub.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetGroupPostsRequest(
    ownerId: Long
) : VKRequest<Long>("wall.get") {

    init {
        addParam("owner_id", ownerId)
    }

    override fun parse(r: JSONObject): Long {
        val items = r.getJSONObject("response")
            .getJSONArray("items")
        val timestamps = mutableListOf<Long>()
        for (i in 0 until items.length()) {
            timestamps.add(
                items.getJSONObject(i).optLong("date")
            )
        }
        return timestamps.max()!! * 1000
    }
}