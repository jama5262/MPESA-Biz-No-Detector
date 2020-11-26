package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.ui.MPESABizNoDetectorActivity
import kotlinx.android.synthetic.main.fragment_results.view.*
import kotlinx.android.synthetic.main.sucess.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val bitmap = requireArguments().getParcelable<Bitmap>(BITMAP)!!
        setUpSharedAnimation()
        setUpImageView(bitmap)
        detect(bitmap)
    }

    private fun setUpSharedAnimation() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move).apply {
                duration = SHARED_ANIM_DURATION
                startDelay = SHARED_ANIM_DELAY
            }
    }

    private fun setUpImageView(bitmap: Bitmap) {
        rootView.apply {
            imageViewDetected.apply {
                val shape = ContextCompat.getDrawable(requireContext(), R.drawable.image_view_shape)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = shape
                } else {
                    background = shape
                }
                setImageBitmap(bitmap)
                transitionName = TRANSITION_NAME
            }
        }
    }

    private fun detect(bitmap: Bitmap) {
        lifecycleScope.launch {
            try {
                showProgress(true)
                val mpesaBizNoDetector =
                    (requireActivity() as MPESABizNoDetectorActivity).mpesaBizNoDetector
                val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
                success(detectedBizNo)
            } catch (e: Exception) {
                fail()
            } finally {
                showProgress(false)
            }
        }
    }

    private fun success(detectedBizNo: DetectedBizNo) {
        rootView.includeSuccess.visibility = View.VISIBLE
        rootView.includeFail.visibility = View.GONE
        val businessNo = detectedBizNo.businessNo.toString()
        val accountNo = detectedBizNo.accountNo ?: ""
        rootView.textFieldBusinessNo.editText!!.setText(businessNo)
        rootView.textFieldAccountNo.editText!!.setText(accountNo)
    }

    private fun fail() {
        rootView.includeFail.visibility = View.VISIBLE
        rootView.includeSuccess.visibility = View.GONE
    }

    private fun showProgress(show: Boolean) {
        val progressBar = rootView.progressBar
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}