package com.arvl.fasimagiland.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arvl.fasimagiland.model.Pencil
import com.arvl.fasimagiland.ui.screen.canvas.CanvasActivity.Companion.currentBrush

class DrawCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TOUCH_TOLERANCE = 4f

    private var mX = 0f
    private var mY = 0f

    private val dataPencil = mutableListOf<Pencil>()
    private val undoList = mutableListOf<Pencil>()
    private var isErasing = false

    private val paintBrush = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = currentBrush
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 16f
        alpha = 0xff
    }

    var undoRedoListener: UndoRedoListener? = null

    fun updateColor(newColor: Int) {
        paintBrush.color = newColor
        currentBrush = newColor
    }

    private fun touchStart(x: Float, y: Float) {
        clearUndoListIfNeeded()
        val path = Path()
        val p = Pencil(paintBrush.color, path)
        dataPencil.add(p)
        path.moveTo(x, y)
        mX = x
        mY = y

        undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            val path = dataPencil.last().path
            path?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
            invalidate()
        }
    }

    private fun touchUp() {
        val path = dataPencil.last().path
        path?.lineTo(mX, mY)
        undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        for (p in dataPencil) {
            paintBrush.color = p.color
            p.path?.let { canvas.drawPath(it, paintBrush) }
        }
        paintBrush.color = currentBrush
    }

    fun startErasing() {
        isErasing = true
        paintBrush.color = Color.WHITE // Atur warna penghapus ke warna background
    }

    fun stopErasing() {
        isErasing = false
        paintBrush.color = currentBrush // Kembalikan warna brush ke warna sebelumnya
    }

    fun undo() {
        if (dataPencil.isNotEmpty()) {
            val removedPencil = dataPencil.removeAt(dataPencil.size - 1)
            undoList.add(removedPencil)
            invalidate()
            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    fun redo() {
        if (undoList.isNotEmpty()) {
            val restoredPencil = undoList.removeAt(undoList.size - 1)
            dataPencil.add(restoredPencil)
            invalidate()
            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    private fun clearUndoListIfNeeded() {
        if (undoList.isNotEmpty()) {
            undoList.clear()
            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    fun canUndo(): Boolean {
        return dataPencil.isNotEmpty()
    }

    fun canRedo(): Boolean {
        return undoList.isNotEmpty()
    }

    fun getBitmapFromCanvas(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }
}
