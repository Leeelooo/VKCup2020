package com.leeloo.vkstore

import android.app.Application
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //since sdk uses v5.90(WHERE IS FAVE) and en by default...
        //why are you guys using old api version and not locale lol
        //or this is secret for hours of debugging
        VK.setConfig(
            VKApiConfig(
                version = "5.103",
                lang = Locale.getDefault().language,
                context = this,
                validationHandler = null
            )
        )
    }
}