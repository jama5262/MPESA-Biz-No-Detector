package com.jama.mpesa_biz_no_detector.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jama.mpesa_biz_no_detector.objectDetection.ObjectDetection
import com.jama.mpesa_biz_no_detector.objectDetection.ObjectReticleGraphic
import com.jama.mpesa_biz_no_detector.utils.NoObjectDetected
import com.jama.mpesa_biz_no_detector.utils.ObjectDetectionException
import com.jama.mpesa_biz_no_detector.utils.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CameraAnalyzer(
    private val scope: CoroutineScope,
    private val graphicOverlay: GraphicOverlay
) : ImageAnalysis.Analyzer {

    private val objectDetection = ObjectDetection()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        val rotation = imageProxy.imageInfo.rotationDegrees

        if (mediaImage != null) {
            scope.launch {
                try {
//                    val bitmap = mediaImage.toBitmap()
//                    val detectedObject = objectDetection.detect(bitmap, rotation)
//                    val boundingBox = detectedObject.second
//                    val imageDim = getImageDimension(imageProxy)
//                    graphicOverlay.updateRect(boundingBox, imageDim)

                    graphicOverlay.clear()
                    graphicOverlay.add(ObjectReticleGraphic())
                } catch (e: NoObjectDetected) {
                    Log.e("jjj", "No object detected clear graph overlay")
                } catch (e: ObjectDetectionException) {
                    Log.e("jjj", "Object detection error -> ${e.message}")
                } catch (e: Exception) {
                    Log.e("jjj", "Error found -> ${e.message}")
                } finally {
                    imageProxy.close()
                }
            }
        }
    }

    private fun getImageDimension(imageProxy: ImageProxy): Pair<Int, Int> {
        return Pair(imageProxy.height, imageProxy.width)
    }
}