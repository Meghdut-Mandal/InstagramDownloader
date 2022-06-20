package com.meghdut.instagram.downloader.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import androidx.core.content.ContextCompat
import android.util.TypedValue
import com.meghdut.instagram.downloader.view.GridItemDecoration
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class GridItemDecoration private constructor(
    private val mHorizonSpan: Int,
    private val mVerticalSpan: Int,
    i3: Int,
    private val mShowLastLine: Boolean
) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable

    override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(canvas, recyclerView)
        drawVertical(canvas, recyclerView)
    }

    private fun drawHorizontal(canvas: Canvas, recyclerView: RecyclerView) {
        val childCount = recyclerView.childCount
        for (i in 0 until childCount) {
            val childAt = recyclerView.getChildAt(i)
            if (!isLastRaw(
                    recyclerView,
                    i,
                    getSpanCount(recyclerView),
                    childCount
                ) || mShowLastLine
            ) {
                val layoutParams = childAt.layoutParams as RecyclerView.LayoutParams
                val bottom = childAt.bottom + layoutParams.bottomMargin
                mDivider.setBounds(
                    childAt.left - layoutParams.leftMargin,
                    bottom,
                    childAt.right + layoutParams.rightMargin,
                    mHorizonSpan + bottom
                )
                mDivider.draw(canvas)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, recyclerView: RecyclerView) {
        val childCount = recyclerView.childCount
        for (i in 0 until childCount) {
            val childAt = recyclerView.getChildAt(i)
            if ((recyclerView.getChildViewHolder(childAt).adapterPosition + 1) % getSpanCount(
                    recyclerView
                ) != 0
            ) {
                val layoutParams = childAt.layoutParams as RecyclerView.LayoutParams
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin + mHorizonSpan
                val right = childAt.right + layoutParams.rightMargin
                val i2 = mVerticalSpan
                var i3 = right + i2
                if (i == childCount - 1) {
                    i3 -= i2
                }
                mDivider.setBounds(right, top, i3, bottom)
                mDivider.draw(canvas)
            }
        }
    }

    // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    override fun getItemOffsets(
        rect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val i: Int
        val spanCount = getSpanCount(recyclerView)
        val itemCount = recyclerView.adapter!!.itemCount
        val viewLayoutPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (viewLayoutPosition < 0) {
            return
        }
        val i2 = viewLayoutPosition % spanCount
        val i3 = mVerticalSpan
        val i4 = i2 * i3 / spanCount
        val i5 = i3 - (i2 + 1) * i3 / spanCount
        i = if (isLastRaw(recyclerView, viewLayoutPosition, spanCount, itemCount)) {
            if (mShowLastLine) mHorizonSpan else 0
        } else {
            mHorizonSpan
        }
        rect[i4, 0, i5] = i
    }

    private fun getSpanCount(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        }
        return if (layoutManager !is StaggeredGridLayoutManager) {
            -1
        } else layoutManager.spanCount
    }

    private fun isLastRaw(recyclerView: RecyclerView, i: Int, i2: Int, i3: Int): Boolean {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            return getResult(i, i2, i3)
        }
        if (layoutManager !is StaggeredGridLayoutManager) {
            return false
        }
        return if (layoutManager.orientation == 1) {
            getResult(i, i2, i3)
        } else (i + 1) % i2 == 0
    }

    private fun getResult(i: Int, i2: Int, i3: Int): Boolean {
        val i4 = i3 % i2
        return if (i4 == 0) i >= i3 - i2 else i >= i3 - i4
    }

    class Builder(private val mContext: Context) {
        private val mResources = mContext.resources
        private var mShowLastLine = true
        private var mHorizonSpan = 0
        private var mVerticalSpan = 0
        private var mColor = -1
        fun setColorResource(i: Int): Builder {
            setColor(ContextCompat.getColor(mContext, i))
            return this
        }

        fun setColor(i: Int): Builder {
            mColor = i
            return this
        }

        fun setVerticalSpan(i: Int): Builder {
            mVerticalSpan = mResources.getDimensionPixelSize(i)
            return this
        }

        fun setVerticalSpan(f: Float): Builder {
            mVerticalSpan = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, f, mResources.displayMetrics).toInt()
            return this
        }

        fun setHorizontalSpan(i: Int): Builder {
            mHorizonSpan = mResources.getDimensionPixelSize(i)
            return this
        }

        fun setHorizontalSpan(f: Float): Builder {
            mHorizonSpan = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, f, mResources.displayMetrics).toInt()
            return this
        }

        fun setShowLastLine(z: Boolean): Builder {
            mShowLastLine = z
            return this
        }

        fun build(): GridItemDecoration {
            return GridItemDecoration(mHorizonSpan, mVerticalSpan, mColor, mShowLastLine)
        }

    }

    init {
        mDivider = ColorDrawable(i3)
    }
}