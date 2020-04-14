package com.leeloo.vkdocuments.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class EditDocumentRequest(
    ownerId: Long = 0L,
    documentId: Long,
    newName: String,
    tags: Array<String> = emptyArray()
) : VKRequest<Boolean>("docs.edit ") {

    init {
        if (ownerId != 0L)
            addParam("owner_id", ownerId)
        addParam("doc_id", documentId)
        addParam("title", newName)
        if (tags.isNotEmpty())
            addParam("tags", tags.joinToString { it })
    }

    override fun parse(r: JSONObject) = r.optInt("response") == 1

}