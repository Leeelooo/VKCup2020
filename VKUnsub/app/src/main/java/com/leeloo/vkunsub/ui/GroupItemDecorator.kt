package com.leeloo.vkunsub.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class GroupItemDecorator(
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = parent.getChildAdapterPosition(view)
        if (view.tag == "item") {
            params.topMargin = margin
            params.leftMargin =
                when {
                    spanIndex % 3 == 0 -> margin
                    spanIndex % 3 == 2 -> margin / 3
                    else -> 2 * margin / 3
                }
            params.rightMargin =
                when {
                    spanIndex % 3 == 0 -> margin / 3
                    spanIndex % 3 == 2 -> margin
                    else -> 2 * margin / 3
                }
        }
    }
}