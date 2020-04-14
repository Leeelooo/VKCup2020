package com.leeloo.vkstore.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

object SmoothOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            0,
            0,
            view.width,
            view.height,
            16f
        )
    }
}