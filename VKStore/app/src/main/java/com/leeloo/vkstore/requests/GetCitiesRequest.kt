package com.leeloo.vkstore.requests

import com.leeloo.vkstore.vo.VKCity
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetCitiesRequest(
    countryId: Long,
    needAll: Int
) : VKRequest<List<VKCity>>("database.getCities") {

    init {
        addParam("country_id", countryId)
        addParam("need_all", needAll)
    }

    override fun parse(r: JSONObject) = VKCity.parse(r)
}