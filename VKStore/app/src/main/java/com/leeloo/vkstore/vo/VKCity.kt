package com.leeloo.vkstore.vo

import org.json.JSONObject

data class VKCity(
    val id: Long,
    val name: String
) {
    companion object {
        fun parse(r: JSONObject): List<VKCity> {
            val cities = mutableListOf<VKCity>()
            val rawCities = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawCities.length()) {
                cities.add(
                    VKCity(
                        id = rawCities.getJSONObject(i).optLong("id"),
                        name = rawCities.getJSONObject(i).optString("title")
                    )
                )
            }
            return cities.toList()
        }
    }
}