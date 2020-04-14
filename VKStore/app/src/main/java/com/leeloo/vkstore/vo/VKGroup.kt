package com.leeloo.vkstore.vo

import org.json.JSONObject

data class VKGroup(
    val id: Long,
    val name: String,
    val type: String,
    val photoUrl: String
) {
    companion object {
        fun parse(r: JSONObject): List<VKGroup> {
            val groups = mutableListOf<VKGroup>()
            val rawGroups = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawGroups.length()) {
                groups.add(
                    VKGroup(
                        id = rawGroups.getJSONObject(i).optLong("id"),
                        name = rawGroups.getJSONObject(i).optString("name"),
                        type = rawGroups.getJSONObject(i).optString("type"),
                        photoUrl = rawGroups.getJSONObject(i).optString("photo_200")
                    )
                )
            }
            return groups.toList()
        }
    }
}