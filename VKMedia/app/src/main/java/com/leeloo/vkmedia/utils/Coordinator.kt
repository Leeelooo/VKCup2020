package com.leeloo.vkmedia.utils

import androidx.fragment.app.FragmentManager
import com.leeloo.vkmedia.R
import com.leeloo.vkmedia.ui.albums.AlbumsFragment
import com.leeloo.vkmedia.ui.details.DetailsFragment
import com.leeloo.vkmedia.ui.login.FragmentLogin
import com.leeloo.vkmedia.vo.VKAlbum
import com.vk.api.sdk.VK

object Coordinator {

    fun onLogin(fm: FragmentManager) {
        fm.beginTransaction()
            .add(R.id.main_activity, AlbumsFragment.newInstance())
            .commitAllowingStateLoss()
    }

    fun onLogout(fm: FragmentManager) {
        VK.logout()
        for (i in 0 until fm.backStackEntryCount)
            fm.popBackStack()
        fm.beginTransaction()
            .replace(R.id.main_activity, FragmentLogin.newInstance())
            .commit()
    }

    fun onAlbumClicked(fm: FragmentManager, album: VKAlbum) {
        fm.beginTransaction()
            .replace(R.id.main_activity, DetailsFragment
                .newInstance(
                    album.id,
                    album.title
                )
            )
            .addToBackStack("ALBUMS")
            .commit()
    }

    fun onDetailsPop(fm: FragmentManager) {
        fm.popBackStack()
    }

}