package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android_animation.AndroidAnimation
import com.example.android_animation.enums.Easing
import com.google.common.util.concurrent.ListenableFuture
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.camera.CameraAnalyzer
import com.jama.mpesa_biz_no_detector.enums.CameraFlowState
import com.jama.mpesa_biz_no_detector.utils.navigateToFragment
import kotlinx.android.synthetic.main.fragment_camera.view.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var rootView: View

    private val cameraViewModel: CameraViewModel by viewModels()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        setUpObservers()
        startCamera()
        lifecycleScope
    }

    private fun setUpObservers() {
        cameraViewModel.cameraFlowState.observe(viewLifecycleOwner) {
            when (it) {
                CameraFlowState.DETECTING, CameraFlowState.DETECTED -> {
                    rootView.textViewMessage.text = getString(R.string.camera_message_1)
                }
                CameraFlowState.CONFIRMING -> {
                    rootView.textViewMessage.text = getString(R.string.camera_message_2)
                }
                CameraFlowState.CONFIRMED -> {
//                    stopCamera()
//                    val bitmap = cameraViewModel.confirmedBitmap.value!!
//                    displayConfirmedImage(bitmap)
                }
            }
        }
    }

    private fun displayConfirmedImage(bitmap: Bitmap) {
        val imageView = ImageView(requireContext())
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.apply {
            bottomToBottom = ConstraintSet.PARENT_ID
            endToEnd  = ConstraintSet.PARENT_ID
            startToStart  = ConstraintSet.PARENT_ID
            topToTop  = ConstraintSet.PARENT_ID
        }
        imageView.apply {
            this.layoutParams = layoutParams
            layoutParams.height = bitmap.height.toDp()
            layoutParams.width = bitmap.width.toDp()
            setImageBitmap(bitmap)
        }
        AndroidAnimation().apply {
            duration = 1000
            easing = Easing.EXP_OUT
            targetViews(imageView)
            scaleX(0.5f, 1f)
            scaleY(0.5f, 1f)
            onAnimationStart {
                rootView.cameraLayout.addView(imageView)
            }
            onAnimationEnd {
                navigateToResult(bitmap)
            }
            start()
        }
    }

    private fun Int.toDp() = (this * resources.displayMetrics.density).toInt() + 50

    private fun startCamera() {
        cameraViewModel.setCameraFlowState(CameraFlowState.DETECTING)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(rootView.viewFinder.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        CameraAnalyzer(cameraViewModel, lifecycleScope, rootView.graphOverlay)
                    )
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("jjj", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun stopCamera() {
        cameraProviderFuture.get().unbindAll()
    }

    private fun navigateToResult(bitmap: Bitmap) {
        val bundle = bundleOf(
            "bitmap" to bitmap
        )

        findNavController().navigateToFragment(
            R.id.action_cameraFragment_to_resultsFragment,
            bundle = bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}