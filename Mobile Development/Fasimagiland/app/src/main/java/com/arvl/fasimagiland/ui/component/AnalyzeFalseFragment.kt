package com.arvl.fasimagiland.ui.component

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.arvl.fasimagiland.R

class AnalyzeFalseFragment : DialogFragment() {

    companion object {
        private const val ARG_IMAGE_BITMAP = "argImageBitmap"

        fun newInstance(imageBitmap: Bitmap): AnalyzeFalseFragment {
            val args = Bundle().apply {
                putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
            }
            val fragment = AnalyzeFalseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analyze_false, container, false)
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

        view.findViewById<View>(R.id.btn_get_started).setOnClickListener {
            // Tambahkan logika untuk menangani klik tombol jika diperlukan
        }
    }
}
