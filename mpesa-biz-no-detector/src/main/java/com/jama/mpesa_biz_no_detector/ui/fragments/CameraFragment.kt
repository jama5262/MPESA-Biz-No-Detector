package com.jama.mpesa_biz_no_detector.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.camera.CameraAnalyzer
import com.jama.mpesa_biz_no_detector.enums.WorkflowState
import kotlinx.android.synthetic.main.fragment_camera.view.*
import kotlinx.coroutines.delay
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
        cameraViewModel.cameraMessage.observe(viewLifecycleOwner) {
            rootView.textViewMessage.text = it
        }

        cameraViewModel.workflowState.observe(viewLifecycleOwner) {
            when(it) {
                WorkflowState.DETECTING, WorkflowState.DETECTED -> {
                    rootView.textViewMessage.text = getString(R.string.camera_message_1)
                }
                WorkflowState.CONFIRMING -> {
                    rootView.textViewMessage.text = getString(R.string.camera_message_2)
                }
                WorkflowState.CONFIRMED -> stopCamera()
            }
        }
    }

    private fun startCamera() {
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
                    it.setAnalyzer(cameraExecutor, CameraAnalyzer(cameraViewModel, lifecycleScope, rootView.graphOverlay))
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

    private fun navigateToSuccess() {
        findNavController().navigate(R.id.action_cameraFragment_to_successFragment)
    }

    private fun navigateToFail() {
        findNavController().navigate(R.id.action_cameraFragment_to_failFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}