package com.leeloo.vkmedia.requests

import com.leeloo.vkmedia.vo.VKPhoto
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetTaggedPhotosRequest(
    userId: Long,
    count: Int,
    sort: Int
) : VKRequest<List<VKPhoto>>("photos.getUserPhotos") {

    init {
        if (userId != 0L)
            addParam("user_id", userId)
        addParam("sort", sort)
        addParam("count", count)
    }

    override fun parse(r: JSONObject) = VKPhoto.parseTagged(r)

}