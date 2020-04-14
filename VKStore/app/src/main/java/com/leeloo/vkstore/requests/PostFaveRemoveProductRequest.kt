package com.leeloo.vkstore.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class PostFaveRemoveProductRequest(
    ownerId: Long,
    productId: Long
) : VKRequest<Boolean>("fave.removeProduct") {

    init {
        addParam("owner_id", ownerId)
        addParam("id", productId)
    }

    override fun parse(r: JSONObject) = r.optInt("response") == 1
}