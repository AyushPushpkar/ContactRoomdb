package com.example.contact

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleSwipeCallback(
    private val adapter: RvAdapter,
    private val swipeLimit: Float = 0.2f // Adjust this value as needed (fraction of itemView width)
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // Enable left and right swipe directions
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Disable move operation
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Notify the adapter that the item has changed to reset its position
        adapter.notifyItemChanged(viewHolder.adapterPosition)
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
        val width = itemView.width.toFloat()

        // Apply swipe limit
        val swipedX = if (dX < 0) Math.max(dX, -width * swipeLimit) else Math.min(dX, width * swipeLimit)

        // Draw the item view with the calculated X position
        itemView.translationX = swipedX

        super.onChildDraw(c, recyclerView, viewHolder, swipedX, dY, actionState, isCurrentlyActive)
    }
}
