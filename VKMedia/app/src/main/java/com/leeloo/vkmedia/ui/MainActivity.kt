package com.leeloo.vkmedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.utils.Coordinator
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback

class MainActivity : AppCompatActivity() {

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            Coordinator.onLogout(supportFragmentManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (VK.isLoggedIn())
                Coordinator.onLogin(supportFragmentManager)
            else
                Coordinator.onLogout(supportFragmentManager)
        }

        VK.addTokenExpiredHandler(tokenTracker)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                Coordinator.onLogin(supportFragmentManager)
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }

        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            for (fragment in supportFragmentManager.fragments)
                fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

}
