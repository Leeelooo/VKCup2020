package com.leeloo.vkmedia.vo

import org.json.JSONObject

data class VKPhoto(
    val id: Long,
    val albumId: Long,
    val previews: List<Size>
) {
    companion object {
        fun parse(r: JSONObject): List<VKPhoto> {
            val photos = mutableListOf<VKPhoto>()
            val rawPhotos = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawPhotos.length()) {
                val rawPhoto = rawPhotos.getJSONObject(i)
                val previews = mutableListOf<Size>()
                if (rawPhoto.has("sizes")) {
                    val rawPreviews = rawPhoto.getJSONArray("sizes")
                    for (j in 0 until rawPreviews.length()) {
                        val rawPreview = rawPreviews.getJSONObject(j)
                        previews.add(
                            Size(
                                src = rawPreview.optString("url"),
                                type = rawPreview.optString("type")[0]
                            )
                        )
                    }
                }
                photos.add(
                    VKPhoto(
                        id = rawPhoto.optLong("id"),
                        albumId = rawPhoto.optLong("album_id"),
                        previews = previews.toList()
                    )
                )
            }
            return photos.toList()
        }

        fun parseTagged(r: JSONObject): List<VKPhoto> {
            val photos = mutableListOf<VKPhoto>()
            val rawPhotos = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawPhotos.length()) {
                val rawPhoto = rawPhotos.getJSONObject(i)
                val previews = listOf(
                    Size(
                        src = rawPhoto.optString("photo_604"),
                        type = 'q'
                    )
                )
                photos.add(
                    VKPhoto(
                        id = rawPhoto.optLong("id"),
                        albumId = rawPhoto.optLong("album_id"),
                        previews = previews
                    )
                )
            }
            return photos.toList()
        }
    }
}