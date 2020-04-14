package com.leeloo.vkmedia.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class CreateAlbumRequest(
    title: String,
    privacyView: String,
    privacyComment: String
) : VKRequest<Boolean>("photos.createAlbum") {

    init {
        addParam("title", title)
        if (privacyView.isNotEmpty())
            addParam("privacy_view", privacyView)
        if (privacyComment.isNotEmpty())
            addParam("privacy_comment", privacyComment)
    }

    override fun parse(r: JSONObject) = r.has("response")

}