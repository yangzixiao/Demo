package com.yzx.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author yzx
 * @date 2019/12/12
 * Description
 */
class LinearItemDecoration(
    private val headSize: Int? = 0,
    private val dividerHeight: Int? = 0,
    private val includeEdg: Boolean? = false,
    private val edg: Int? = 0,
    @ColorRes private var dividerColor: Int? = 0x00000000
) :
    RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)
        //HeadView不处理
        if (position < headSize!!) {
            return
        }

        val adjPosition = position - headSize

        if (includeEdg!!) {
            if (adjPosition == 0) {
                outRect.top=dividerHeight!!
            }
            outRect.bottom = dividerHeight!!
            outRect.left = edg!!
            outRect.right = edg
        } else {
            if (adjPosition != 0) {
                outRect.top=dividerHeight!!
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        
    }

}