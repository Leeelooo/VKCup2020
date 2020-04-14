package com.leeloo.vkunsub.utils

import android.view.View

fun reduce(subs: Long): String {
    if (subs / 1000 == 0L)
        return "$subs"
    var subsReduced = subs.toDouble()
    var reduces = 0
    while (subsReduced / 1000 > 1 && reduces < 2) {
        subsReduced /= 1000
        reduces++
    }
    val reducedNumber = (subsReduced * 10).toInt() / 10.0
    return "$reducedNumber${
    when (reduces) {
        1 -> "K"
        else -> "M"
    }
    }"
}

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }