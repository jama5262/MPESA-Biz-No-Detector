package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jama.mpesa_biz_no_detector.R

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

        return rootView
    }
}