package com.leeloo.vkmedia.vo

import org.json.JSONObject

data class VKAlbum(
    val id: Long,
    val ownerId: Long,
    val title: String,
    val size: Int,
    val previews: List<Size>
) {
    companion object {
        fun parse(r: JSONObject): List<VKAlbum> {
            val albums = mutableListOf<VKAlbum>()
            val rawAlbums = r.getJSONObject("response").getJSONArray("items")
            for (i in 0 until rawAlbums.length()) {
                val rawAlbum = rawAlbums.getJSONObject(i)
                val previews = mutableListOf<Size>()
                if (rawAlbum.has("sizes")) {
                    val rawPreviews = rawAlbum.getJSONArray("sizes")
                    for (j in 0 until rawPreviews.length()) {
                        val rawPhoto = rawPreviews.getJSONObject(j)
                        previews.add(
                            Size(
                                src = rawPhoto.optString("src"),
                                type = rawPhoto.optString("type")[0]
                            )
                        )
                    }
                }
                albums.add(
                    VKAlbum(
                        id = rawAlbum.optLong("id"),
                        ownerId = rawAlbum.optLong("owner_id"),
                        title = rawAlbum.optString("title"),
                        size = rawAlbum.optInt("size"),
                        previews = previews.toList()
                    )
                )
            }
            return albums.toList()
        }
    }
}