package com.leeloo.vkdocuments.vo

import org.json.JSONObject

data class VKDocument(
    val id: Long,
    val ownerId: Long,
    val title: String,
    val size: Long,
    val ext: String,
    val url: String,
    val date: Long,
    val type: Int,
    val preview: List<Photo>,
    val tags: List<String>
) {
    companion object {
        fun parse(r: JSONObject): List<VKDocument> {
            val rawResponse = r.getJSONObject("response")
            val docs = mutableListOf<VKDocument>()
            for (i in 0 until rawResponse.getJSONArray("items").length()) {
                val rawDoc = rawResponse.getJSONArray("items").getJSONObject(i)
                val preview = mutableListOf<Photo>()
                if (rawDoc.has("preview")) {
                    val photos = rawDoc.getJSONObject("preview")
                        .getJSONObject("photo")
                        .getJSONArray("sizes")
                    for (j in 0 until photos.length()) {
                        val rawPhoto = photos.getJSONObject(j)
                        preview.add(
                            Photo(
                                src = rawPhoto.optString("src"),
                                type = rawPhoto.optString("type")[0]
                            )
                        )
                    }
                }
                val tags = mutableListOf<String>()
                if (rawDoc.has("tags")) {
                    val rawTags = rawDoc.getJSONArray("tags")
                    for (j in 0 until rawTags.length()) {
                        tags.add(rawTags.optString(j))
                    }
                }

                val doc = VKDocument(
                    id = rawDoc.optLong("id"),
                    ownerId = rawDoc.optLong("owner_id"),
                    title = rawDoc.optString("title"),
                    size = rawDoc.optLong("size"),
                    ext = rawDoc.optString("ext"),
                    url = rawDoc.optString("url"),
                    date = rawDoc.optLong("date"),
                    type = rawDoc.optInt("type"),
                    preview = preview,
                    tags = tags
                )
                docs.add(doc)
            }
            return docs.toList()
        }
    }
}