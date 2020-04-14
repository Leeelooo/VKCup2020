package com.leeloo.vkmedia.requests

import android.net.Uri
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class VKServerUploadInfo(val uploadUrl: String, val albumId: Int, val userId: Int)

class VKFileUploadInfo(val server: String, val photos: String, val hash: String)

class PostPhotoAlbumsRequest(
    private val albumId: Long,
    private val groupId: Long,
    private val uri: Uri
) : ApiCommand<Int>() {
    override fun onExecute(manager: VKApiManager): Int {
        val uploadInfo = getServerUploadInfo(manager, albumId)

        val fileUploadCall = VKHttpPostCall.Builder()
            .url(uploadInfo.uploadUrl)
            .args("photo", uri, "image.jpg")
            .timeout(TimeUnit.MINUTES.toMillis(5))
            .retryCount(RETRY_COUNT)
            .build()
        val fileUploadInfo = manager.execute(fileUploadCall, null, FileUploadInfoParser())

        val saveCallBuilder = VKMethodCall.Builder()
            .method("photos.save")
            .args("album_id", albumId)
            .args("server", fileUploadInfo.server)
            .args("photos_list", fileUploadInfo.photos)
            .args("hash", fileUploadInfo.hash)
            .version(manager.config.version)

        if (groupId != 0L)
            saveCallBuilder.args("group_id", groupId)

        return manager.execute(saveCallBuilder.build(), SaveInfoParser())
    }

    private fun getServerUploadInfo(manager: VKApiManager, albumId: Long): VKServerUploadInfo {
        val uploadInfoCall = VKMethodCall.Builder()
            .method("photos.getUploadServer")
            .args("album_id", albumId)
            .version(manager.config.version)
            .build()
        return manager.execute(uploadInfoCall, ServerUploadInfoParser())
    }

    companion object {
        const val RETRY_COUNT = 3
    }

    private class ServerUploadInfoParser : VKApiResponseParser<VKServerUploadInfo> {
        override fun parse(response: String): VKServerUploadInfo {
            try {
                val joResponse = JSONObject(response).getJSONObject("response")
                return VKServerUploadInfo(
                    uploadUrl = joResponse.getString("upload_url"),
                    albumId = joResponse.getInt("album_id"),
                    userId = joResponse.getInt("user_id")
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class FileUploadInfoParser : VKApiResponseParser<VKFileUploadInfo> {
        override fun parse(response: String): VKFileUploadInfo {
            try {
                val joResponse = JSONObject(response)
                return VKFileUploadInfo(
                    server = joResponse.getString("server"),
                    photos = joResponse.getString("photos_list"),
                    hash = joResponse.getString("hash")
                )
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }

    private class SaveInfoParser : VKApiResponseParser<Int> {
        override fun parse(response: String): Int {
            try {
                val joResponse = JSONObject(response).getJSONArray("response").getJSONObject(0)
                return joResponse.getInt("id")
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}