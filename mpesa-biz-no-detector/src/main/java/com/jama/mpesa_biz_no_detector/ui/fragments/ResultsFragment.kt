package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.android_animation.AndroidAnimation
import com.example.android_animation.enums.Easing
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.states.BizNoType
import com.jama.mpesa_biz_no_detector.states.ResultsState
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

    private var businessNo = ""
    private var accountNo = ""

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
        setUpViews()
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

    private fun setUpViews() {
        rootView.buttonProceed.setOnClickListener {
            val activity = (requireActivity() as MPESABizNoDetectorActivity)
            when (val resultsState = resultsViewModel.resultState.value!!) {
                is ResultsState.Success -> {
                    val type = resultsState.detectedBizNo.type
                    if (areTextFieldsEmpty(type)) return@setOnClickListener
                    val detectedBizNo = DetectedBizNo(
                        type,
                        businessNo.toInt(),
                        if (accountNo.isNotBlank()) accountNo else null
                    )
                    activity.sendResults(detectedBizNo)
                }
            }
        }

        rootView.buttonRetry.setOnClickListener {
            findNavController().navigateUp()
        }

        rootView.textFieldBusinessNo.editText!!.doOnTextChanged { text, _, _, _ ->
            rootView.textFieldBusinessNo.error = ""
            businessNo = text.toString()
        }

        rootView.textFieldAccountNo.editText!!.doOnTextChanged { text, _, _, _ ->
            rootView.textFieldAccountNo.error = ""
            accountNo = text.toString()
        }
    }

    private fun setUpObservers() {
        resultsViewModel.showProgress.observe(viewLifecycleOwner) {
            val progressBar = rootView.progressBar
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        resultsViewModel.resultState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultsState.Success -> success(it.detectedBizNo)
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
        rootView.buttonProceed.visibility = View.VISIBLE
        rootView.buttonRetry.visibility = View.VISIBLE
        val businessNo = detectedBizNo.businessNo.toString()
        rootView.textFieldBusinessNo.editText!!.setText(businessNo)
        this.businessNo = businessNo
        when (detectedBizNo.type) {
            BizNoType.PAYBILL_NUMBER -> {
                rootView.apply {
                    textViewType.text = getString(R.string.paybill)
                    textFieldAccountNo.apply {
                        val accountNo = detectedBizNo.accountNo ?: ""
                        visibility = View.VISIBLE
                        editText!!.setText(accountNo)
                        this@ResultsFragment.accountNo = accountNo
                    }
                }
            }
            BizNoType.TILL_NUMBER -> {
                rootView.apply {
                    textViewType.text = getString(R.string.buy_goods_and_services)
                    textFieldAccountNo.visibility = View.GONE
                }
            }
        }
        animate(rootView.includeSuccess)
    }

    private fun fail() {
        rootView.includeFail.visibility = View.VISIBLE
        rootView.includeSuccess.visibility = View.GONE
        rootView.buttonRetry.visibility = View.VISIBLE
        animate(rootView.includeFail)
    }

    private fun animate(targetView: View) {
        AndroidAnimation().apply {
            duration = 1500
            easing = Easing.EXP_OUT
            targetViews(targetView)
            translateX(rootView.width.toFloat(), 0f)
            start()
        }
    }

    private fun areTextFieldsEmpty(type: BizNoType): Boolean {
        var isBusinessNoEmpty = false
        var isAccountNoEmpty = false

        if (businessNo.isBlank()) {
            isBusinessNoEmpty = true
            rootView.textFieldBusinessNo.error = getString(R.string.please_add_a_biz_no)
        }

        if (accountNo.isBlank() && type == BizNoType.PAYBILL_NUMBER) {
            isAccountNoEmpty = true
            rootView.textFieldAccountNo.error = getString(R.string.please_add_an_acc_no)
        }

        return isBusinessNoEmpty || isAccountNoEmpty
    }
}