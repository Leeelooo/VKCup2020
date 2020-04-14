package com.leeloo.vkmedia.requests

import com.leeloo.vkmedia.vo.VKAlbum
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetAlbumsRequest(
    ownerId: Long,
    albumsId: IntArray,
    offset: Int,
    count: Int,
    needSystem: Int,
    needCovers: Int,
    photoSizes: Int
) : VKRequest<List<VKAlbum>>("photos.getAlbums") {

    init {
        if (ownerId != 0L)
            addParam("owner_id", ownerId)
        if (albumsId.isNotEmpty())
            addParam("albums_id", albumsId)
        addParam("offset", offset)
        addParam("count", count)
        addParam("need_system", needSystem)
        addParam("need_covers", needCovers)
        addParam("photo_sizes", photoSizes)
    }

    override fun parse(r: JSONObject) = VKAlbum.parse(r)

}