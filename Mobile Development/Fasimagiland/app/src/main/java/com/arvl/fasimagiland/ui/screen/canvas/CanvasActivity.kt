package com.arvl.fasimagiland.ui.screen.canvas

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding
import com.arvl.fasimagiland.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class CanvasActivity : AppCompatActivity(), UndoRedoListener, ImageClassifierHelper.ClassifierListener {
    private val binding: ActivityCanvasBinding by lazy {
        ActivityCanvasBinding.inflate(layoutInflater)
    }

    private var isPencilIconClicked = false
    private var isEraserIconClicked = false
    private var isPaletteIconClicked = false

    companion object {
        var path = Path()
        var currentBrush = Color.BLACK
        var isEraserActive = false
    }

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Inisialisasi ImageClassifierHelper
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )

        val firstSentence = intent.getStringExtra("FIRST_SENTENCE")
        firstSentence?.let {
            binding.lstory.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.apply {
            drawPencil.undoRedoListener = this@CanvasActivity

            btnPencil.setOnClickListener {
                isPencilIconClicked = !isPencilIconClicked

                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)

                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)

                    drawPencil.stopErasing()
                    isEraserActive = false

                } else {
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                }
            }

            btnEraser.setOnClickListener {
                isEraserIconClicked = !isEraserIconClicked
                if (isEraserIconClicked) {
                    btnEraser.setImageResource(R.drawable.ic_selected_eraser)

                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                    colorPalate.visibility = View.INVISIBLE
                    drawPencil.startErasing()
                    isEraserActive = true
                } else {
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    drawPencil.stopErasing()
                    isEraserActive = false
                }
            }

            btnRedo.setOnClickListener {
                drawPencil.redo()
                updateUndoRedoIcons()
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
            }

            btnUndo.setOnClickListener {
                drawPencil.undo()
                updateUndoRedoIcons()
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
            }

            btnPallete.setOnClickListener {
                isPaletteIconClicked = !isPaletteIconClicked
                if (isPaletteIconClicked) {
                    colorPalate.visibility = View.VISIBLE
                    btnPallete.setImageResource(R.drawable.ic_selected_palette)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                } else {
                    colorPalate.visibility = View.INVISIBLE
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                }
            }

            // Handle color selection
            btnBlue.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_blue)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnRed.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_red)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnYellow.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_yellow)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnGreen.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_green)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnBlack.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_black)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            binding.btnAnalyze.setOnClickListener {
                val bitmap = drawPencil.getBitmapFromCanvas()
                bitmap.let {
                    imageClassifierHelper.classifyCanvas(it)
                }
            }
        }
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        results?.let { classifications ->
            // Flatten the list of classifications and sort them based on the score
            val sortedResults = classifications
                .flatMap { it.categories }
                .sortedByDescending { it.score }

            // Take the top 5 results
            val top5Results = sortedResults.take(5)

            if (top5Results.isNotEmpty()) {
                // Print the top 5 results to log
                Log.d(TAG, "Top 5 Predictions:")
                top5Results.forEachIndexed { index, category ->
                    Log.d(TAG, "${index + 1}. ${category.label}: ${"%.2f".format(category.score)}")
                }
            } else {
                // If no top results are found, print a failure message to log
                Log.d(TAG, "Klasifikasi gagal: No top results found")
            }
        } ?: run {
            // If results are null, print a failure message to log
            Log.d(TAG, "Klasifikasi gagal: Results are null")
        }
    }



    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun updateUndoRedoIcons() {
        binding.apply {
            if (drawPencil.canUndo()) {
                btnUndo.setImageResource(R.drawable.ic_selected_undo)
            } else {
                btnUndo.setImageResource(R.drawable.ic_unselected_undo)
            }

            if (drawPencil.canRedo()) {
                btnRedo.setImageResource(R.drawable.ic_selected_redo)
            } else {
                btnRedo.setImageResource(R.drawable.ic_unselected_redo)
            }
        }
    }

    override fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        binding.btnUndo.isEnabled = canUndo
        binding.btnRedo.isEnabled = canRedo
        updateUndoRedoIcons()
    }
}
