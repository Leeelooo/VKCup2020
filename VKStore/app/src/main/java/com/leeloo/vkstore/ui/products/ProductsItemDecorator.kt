package com.leeloo.vkstore.ui.products

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ProductsItemDecorator(
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (view.tag == "item") {
            params.topMargin = margin
            params.leftMargin = if (params.spanIndex % 2 == 0) margin else margin / 2
            params.rightMargin = if (params.spanIndex % 2 == 1) margin else margin / 2
            params.bottomMargin = 0
        }
    }
}