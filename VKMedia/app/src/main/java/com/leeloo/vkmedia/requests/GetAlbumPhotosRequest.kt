package com.leeloo.vkmedia.requests

import com.leeloo.vkmedia.vo.VKPhoto
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetAlbumPhotosRequest(
    ownerId: Long,
    albumId: String,
    rev: Int,
    photoSizes: Int,
    offset: Int,
    count: Int
) : VKRequest<List<VKPhoto>>("photos.get") {

    init {
        if (ownerId != 0L)
            addParam("owner_id", ownerId)
        if (albumId.isNotEmpty())
            addParam("album_id", albumId)
        addParam("rev", rev)
        addParam("photo_sizes", photoSizes)
        addParam("offset", offset)
        addParam("count", count)
    }

    override fun parse(r: JSONObject) = VKPhoto.parse(r)

}