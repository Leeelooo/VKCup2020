package com.leeloo.vkdocuments.requests

import com.leeloo.vkdocuments.vo.VKDocument
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetDocumentRequest(
    count: Int,
    offset: Int,
    type: Int,
    returnTags: Int,
    ownerId: Long = 0L
) : VKRequest<List<VKDocument>>("docs.get") {

    init {
        addParam("count", count)
        addParam("offset", offset)
        addParam("type", type)
        addParam("return_tags", returnTags)
        if (ownerId != 0L)
            addParam("owner_id", ownerId)
    }

    override fun parse(r: JSONObject) = VKDocument.parse(r)

}