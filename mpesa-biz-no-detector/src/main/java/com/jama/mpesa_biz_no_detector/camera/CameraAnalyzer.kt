package com.jama.mpesa_biz_no_detector.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jama.mpesa_biz_no_detector.graphics.GraphicOverlay
import com.jama.mpesa_biz_no_detector.graphics.GraphicsOverlayController
import com.jama.mpesa_biz_no_detector.objectDetection.ObjectDetection
import com.jama.mpesa_biz_no_detector.ui.fragments.CameraViewModel
import com.jama.mpesa_biz_no_detector.utils.ObjectDetectionException
import com.jama.mpesa_biz_no_detector.utils.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CameraAnalyzer(
    cameraViewModel: CameraViewModel,
    private val scope: CoroutineScope,
    graphicOverlay: GraphicOverlay,
    private val catchErrorAction: () -> Unit
) : ImageAnalysis.Analyzer {

    private val objectDetection = ObjectDetection()
    private val graphicsOverlayController = GraphicsOverlayController(cameraViewModel, graphicOverlay)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        val rotation = imageProxy.imageInfo.rotationDegrees

        if (mediaImage != null) {
            scope.launch {
                try {
                    val bitmap = mediaImage.toBitmap()
                    val detectionResult = objectDetection.detect(bitmap, rotation)
                    graphicsOverlayController.start(detectionResult, getImageDimension(imageProxy))
                } catch (e: Exception) {
                    catchErrorAction()
                } finally {
                    imageProxy.close()
                }
            }
        }
    }

    private fun getImageDimension(imageProxy: ImageProxy): Pair<Float, Float> {
        return Pair(imageProxy.height.toFloat(), imageProxy.width.toFloat())
    }
}