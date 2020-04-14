package com.leeloo.vkunsub.vo

import org.json.JSONObject

data class VKGroup(
    val id: Long,
    val name: String,
    val screenName: String,
    val description: String,
    val membersCount: Long,
    val photoUrl: String,
    var lastTimestamp: Long = 0L
) {
    companion object {
        fun parse(r: JSONObject): List<VKGroup> {
            val groups = mutableListOf<VKGroup>()
            val rawGroups = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawGroups.length()) {
                val rawGroup = rawGroups.getJSONObject(i)
                if (rawGroup.optString("type") != "profile")
                    groups.add(
                        VKGroup(
                            id = rawGroup.optLong("id"),
                            name = rawGroup.optString("name"),
                            screenName = rawGroup.optString("screen_name"),
                            description = rawGroup.optString("description"),
                            membersCount = rawGroup.optLong("members_count"),
                            photoUrl = rawGroup.optString("photo_200")
                        )
                    )
            }
            return groups.toList()
        }
    }
}