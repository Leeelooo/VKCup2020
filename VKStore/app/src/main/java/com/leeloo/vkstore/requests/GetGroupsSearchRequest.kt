package com.leeloo.vkstore.requests

import com.leeloo.vkstore.vo.VKGroup
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetGroupsSearchRequest(
    queue: String,
    cityId: Long,
    count: Int,
    market: Int,
    sort: Int
) : VKRequest<List<VKGroup>>("groups.search") {

    init {
        addParam("q", queue)
        addParam("city_id", cityId)
        addParam("market", market)
        addParam("count", count)
        addParam("sort", sort)
    }

    override fun parse(r: JSONObject) = VKGroup.parse(r)
}