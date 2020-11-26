package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jama.mpesa_biz_no_detector.R
import kotlinx.android.synthetic.main.fragment_results.view.*

class ResultsFragment : Fragment() {

    private companion object {
        const val BITMAP = "bitmap"
    }

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_results, container, false)

        Log.e("jjj", "bitmap is ${requireArguments().getParcelable<Bitmap>(BITMAP)}")
        val bitmap = requireArguments().getParcelable<Bitmap>(BITMAP)
        rootView.imageView.apply {
            val shape = ContextCompat.getDrawable(requireContext(), R.drawable.image_view_shape)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = shape
            } else {
                background = shape
            }
            setImageBitmap(bitmap)
        }

        return rootView
    }
}