package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.states.BizNoType
import com.jama.mpesa_biz_no_detector.states.ResultsState
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.ui.MPESABizNoDetectorActivity
import kotlinx.android.synthetic.main.fragment_results.view.*
import kotlinx.android.synthetic.main.sucess.view.*
import kotlinx.coroutines.launch

class ResultsFragment : Fragment() {

    private companion object {
        const val BITMAP = "bitmap"
        const val SHARED_ANIM_DURATION = 400L
        const val SHARED_ANIM_DELAY = 400L
        const val TRANSITION_NAME = "imageViewDetected"
    }

    private lateinit var rootView: View

    private val resultsViewModel: ResultsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_results, container, false)
        initialize()
        return rootView
    }

    private fun initialize() {
        setUpObservers()
        val bitmap = requireArguments().getParcelable<Bitmap>(BITMAP)!!
        setUpSharedAnimation()
        setUpImageView(bitmap)
        setUpButtons()
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

    private fun setUpButtons() {
        rootView.buttonProceed.setOnClickListener {

        }

        rootView.buttonRetry.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpObservers() {
        resultsViewModel.showProgress.observe(viewLifecycleOwner) {
            val progressBar = rootView.progressBar
            val buttonProceed = rootView.buttonProceed
            val buttonRetry = rootView.buttonRetry
            when (it) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    buttonProceed.visibility = View.GONE
                    buttonRetry.visibility = View.GONE
                }
                false -> {
                    progressBar.visibility = View.GONE
                    buttonProceed.visibility = View.VISIBLE
                    buttonRetry.visibility = View.VISIBLE
                }
            }
        }

        resultsViewModel.resultState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultsState.Success -> {
                    success(it.detectedBizNo)
                }
                is ResultsState.Fail -> fail()
            }
        }
    }

    private fun detect(bitmap: Bitmap) {
        lifecycleScope.launch {
            try {
                resultsViewModel.showProgress(true)
                val mpesaBizNoDetector =
                    (requireActivity() as MPESABizNoDetectorActivity).mpesaBizNoDetector
                val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
                resultsViewModel.setResultState(ResultsState.Success(detectedBizNo))
            } catch (e: Exception) {
                resultsViewModel.setResultState(ResultsState.Fail)
            } finally {
                resultsViewModel.showProgress(false)
            }
        }
    }

    private fun success(detectedBizNo: DetectedBizNo) {
        rootView.includeSuccess.visibility = View.VISIBLE
        rootView.includeFail.visibility = View.GONE
        val businessNo = detectedBizNo.businessNo.toString()
        rootView.includeSuccess.textFieldBusinessNo.editText!!.setText(businessNo)
        when (detectedBizNo.type) {
            BizNoType.PAYBILL_NUMBER -> {
                rootView.includeSuccess.apply {
                    textViewType.text = getString(R.string.paybill)
                    textFieldAccountNo.apply {
                        val accountNo = detectedBizNo.accountNo ?: ""
                        visibility = View.VISIBLE
                        editText!!.setText(accountNo)
                    }
                }
            }
            BizNoType.TILL_NUMBER -> {
                rootView.includeSuccess.apply {
                    textViewType.text = getString(R.string.buy_goods_and_services)
                    textFieldAccountNo.visibility = View.GONE
                }
            }
        }
    }

    private fun fail() {
        rootView.includeFail.visibility = View.VISIBLE
        rootView.includeSuccess.visibility = View.GONE
    }

    private fun showProgress(show: Boolean) {

    }
}