package com.arvl.fasimagiland.ui.component

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.arvl.fasimagiland.R

class AnalyzeFragment : DialogFragment() {

    companion object {
        private const val ARG_IMAGE_BITMAP = "argImageBitmap"

        fun newInstance(imageBitmap: Bitmap): AnalyzeFragment {
            val args = Bundle().apply {
                putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
            }
            val fragment = AnalyzeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageBitmap = arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP)

        imageBitmap?.let {
            val imageView = view.findViewById<ImageView>(R.id.iv_analyze)
            imageView.setImageBitmap(it)
        }

        val btnGetStarted = view.findViewById<Button>(R.id.btn_get_started)
        btnGetStarted.setOnClickListener {
            // Tambahkan logika untuk menangani klik tombol jika diperlukan
        }
    }
}
