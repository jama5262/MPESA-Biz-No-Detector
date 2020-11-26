package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.transition.TransitionInflater
import com.jama.mpesa_biz_no_detector.R
import kotlinx.android.synthetic.main.fragment_results.view.*

class ResultsFragment : Fragment() {

    private companion object {
        const val BITMAP = "bitmap"
        const val SHARED_ANIM_DURATION = 400L
        const val SHARED_ANIM_DELAY = 400L
        const val TRANSITION_NAME = "imageViewDetected"
    }

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_results, container, false)
        initialize()
        return rootView
    }

    private fun initialize() {
        setUpSharedAnimation()
        setUpImageView()
    }

    private fun setUpSharedAnimation() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move).apply {
                duration = SHARED_ANIM_DURATION
                startDelay = SHARED_ANIM_DELAY
            }
    }

    private fun setUpImageView() {
        rootView.apply {
            imageViewDetected.apply {
                val shape = ContextCompat.getDrawable(requireContext(), R.drawable.image_view_shape)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = shape
                } else {
                    background = shape
                }
                val bitmap = requireArguments().getParcelable<Bitmap>(BITMAP)
                setImageBitmap(bitmap)
                transitionName = TRANSITION_NAME
            }
        }
    }
}