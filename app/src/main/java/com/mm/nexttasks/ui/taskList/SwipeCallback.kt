package com.mm.nexttasks.ui.taskList

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mm.nexttasks.R

private const val VIEW_TYPE_ITEM = 1
private const val VIEW_TYPE_SEPARATOR = 2

abstract class SwipeCallback(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val mContext: Context = context
    private val deleteIcon = ContextCompat.getDrawable(mContext, R.drawable.recycle_bin_24)!!
    private val background = ColorDrawable()
    private val backgroundColor = ContextCompat.getColor(mContext, R.color.danger)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder.itemViewType == VIEW_TYPE_SEPARATOR) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemViewHeightNoMargin = itemView.height - dpToPixel(17.0f).toInt()

        val isCanceled = dX == 0f && !isCurrentlyActive
        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom - dpToPixel(17.0f).toInt()
        )
        background.draw(c)

        val iconTop = itemView.top + (itemViewHeightNoMargin - deleteIcon.intrinsicHeight) / 2
        val iconMargin = (itemViewHeightNoMargin - deleteIcon.intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + deleteIcon.intrinsicHeight

        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    private fun dpToPixel(dp: Float): Float {
        val metrics = mContext.resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px
    }
}