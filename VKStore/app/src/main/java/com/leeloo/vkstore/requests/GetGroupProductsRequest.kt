package com.leeloo.vkstore.requests

import com.leeloo.vkstore.vo.VKProduct
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetGroupProductsRequest(
    ownerId: Long,
    count: Int
) : VKRequest<List<VKProduct>>("market.get") {

    init {
        addParam("owner_id", ownerId)
        addParam("count", count)
    }

    override fun parse(r: JSONObject) = VKProduct.parse(r)

}