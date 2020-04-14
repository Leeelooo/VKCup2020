package com.leeloo.vkmedia.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class DeleteAlbumRequest(
    albumId: Long,
    groupId: Long
) : VKRequest<Boolean>("photos.deleteAlbum") {

    init {
        addParam("album_id", albumId)
        if (groupId != 0L)
            addParam("group_id", groupId)
    }

    override fun parse(r: JSONObject) = r.optInt("response") == 1

}