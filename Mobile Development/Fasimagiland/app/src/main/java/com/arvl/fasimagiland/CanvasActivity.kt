package com.arvl.fasimagiland

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding

// CanvasActivity.kt
class CanvasActivity : AppCompatActivity(), UndoRedoListener {
    private val binding: ActivityCanvasBinding by lazy {
        ActivityCanvasBinding.inflate(layoutInflater)
    }

    private var isPencilIconClicked = false
    private var isEraserIconClicked = false
    private var isRedoIconClicked = false
    private var isCircleIconClicked = false
    private var isPaletteIconClicked = false

    companion object {
        var path = Path()
        var paintBrush = Paint()
        var colorList = ArrayList<Int>()
        var currentBrush = Color.BLACK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            drawPencil.undoRedoListener = this@CanvasActivity

            btnPencil.setOnClickListener {
                // Untuk mengganti dari false menjadi true
                isPencilIconClicked = !isPencilIconClicked

                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)

                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnRedo.setImageResource(R.drawable.ic_unselected_redo)
                    btnUndo.setImageResource(R.drawable.ic_unselected_undo)
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)

                    drawPencil.visibility = View.VISIBLE

                } else {
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                }
            }

            btnEraser.setOnClickListener {
                isEraserIconClicked = !isEraserIconClicked
                if (isEraserIconClicked) {
                    btnEraser.setImageResource(R.drawable.ic_selected_eraser)

                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    btnRedo.setImageResource(R.drawable.ic_unselected_redo)
                    btnUndo.setImageResource(R.drawable.ic_unselected_undo)
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                } else {
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                }
            }

            btnRedo.setOnClickListener {
                drawPencil.redo()
            }

            btnUndo.setOnClickListener {
                drawPencil.undo()
            }

            btnPallete.setOnClickListener {
                isPaletteIconClicked = !isPaletteIconClicked

                if (isPaletteIconClicked) {
                    colorPalate.visibility = View.VISIBLE

                    btnPallete.setImageResource(R.drawable.ic_selected_palette)

                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnRedo.setImageResource(R.drawable.ic_unselected_redo)
                    btnUndo.setImageResource(R.drawable.ic_unselected_undo)
                } else {
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                    colorPalate.visibility = View.INVISIBLE
                }
            }

            btnBlue.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.palette_blue)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnRed.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.palette_red)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnYellow.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.palette_yellow)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnGreen.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.palette_green)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnBlack.setOnClickListener {
                paintBrush.color = Color.BLACK
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }
        }
    }

    override fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        binding.btnUndo.apply {
            setImageResource(if (canUndo) R.drawable.ic_selected_undo else R.drawable.ic_unselected_undo)
        }

        binding.btnRedo.apply {
            setImageResource(if (canRedo) R.drawable.ic_selected_redo else R.drawable.ic_unselected_redo)
        }
    }

    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }
}
