package com.leeloo.vkstore.vo

import org.json.JSONObject
import java.io.Serializable

data class VKProduct(
    val id: Long,
    val ownerId: Long,
    val title: String,
    val description: String,
    val price: String,
    val photoUrl: String,
    var isFavorite: Boolean
) : Serializable {
    companion object {
        fun parse(r: JSONObject): List<VKProduct> {
            val products = mutableListOf<VKProduct>()
            val rawProducts = r.getJSONObject("response")
                .getJSONArray("items")
            for (i in 0 until rawProducts.length()) {
                val rawProduct = rawProducts.getJSONObject(i)
                products.add(
                    VKProduct(
                        id = rawProduct.optLong("id"),
                        ownerId = rawProduct.optLong("owner_id"),
                        title = rawProduct.optString("title"),
                        description = rawProduct.optString("description"),
                        price = rawProduct.getJSONObject("price").optString("text"),
                        photoUrl = rawProduct.optString("thumb_photo"),
                        isFavorite = rawProduct.has("is_favorite")
                    )
                )
            }
            return products.toList()
        }
    }
}