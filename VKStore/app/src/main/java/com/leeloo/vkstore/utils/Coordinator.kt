package com.leeloo.vkstore.utils

import androidx.fragment.app.FragmentManager
import com.leeloo.vkstore.R
import com.leeloo.vkstore.ui.details.DetailsFragment
import com.leeloo.vkstore.ui.groups.GroupsFragment
import com.leeloo.vkstore.ui.login.LoginFragment
import com.leeloo.vkstore.ui.products.ProductFragment
import com.leeloo.vkstore.vo.VKGroup
import com.leeloo.vkstore.vo.VKProduct
import com.vk.api.sdk.VK

object Coordinator {

    fun onLogin(fm: FragmentManager) {
        fm.beginTransaction()
            .replace(R.id.main_activity, GroupsFragment.newInstance())
            .commitAllowingStateLoss()
    }

    fun onLogout(fm: FragmentManager) {
        VK.logout()
        fm.beginTransaction()
            .replace(R.id.main_activity, LoginFragment.newInstance())
            .commit()
    }

    fun onGroupClicked(fm: FragmentManager, group: VKGroup) {
        fm.beginTransaction()
            .replace(R.id.main_activity, ProductFragment
                .newInstance(
                    group.id,
                    group.name
                )
            )
            .addToBackStack("GROUPS")
            .commit()
    }

    fun onProductClicked(fm: FragmentManager, product: VKProduct) {
        fm.beginTransaction()
            .replace(R.id.main_activity, DetailsFragment
                .newInstance(product)
            )
            .addToBackStack("PRODUCTS")
            .commit()
    }

    fun onPopBackstack(fm: FragmentManager) {
        fm.popBackStack()
    }

}