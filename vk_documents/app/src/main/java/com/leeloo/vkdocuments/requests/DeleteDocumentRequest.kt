package com.leeloo.vkdocuments.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class DeleteDocumentRequest(
    ownerId: Long = 0L,
    documentId: Long
) : VKRequest<Boolean>("docs.delete") {

    init {
        if (ownerId != 0L)
            addParam("owner_id", ownerId)
        addParam("doc_id", documentId)
    }

    override fun parse(r: JSONObject) =
        r.optInt("response") == 1

}