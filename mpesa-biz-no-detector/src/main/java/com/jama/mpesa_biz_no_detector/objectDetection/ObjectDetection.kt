package com.jama.mpesa_biz_no_detector.objectDetection

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jama.mpesa_biz_no_detector.utils.ObjectDetectionException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ObjectDetection {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .build()

    private val objectDetector = ObjectDetection.getClient(options)

    suspend fun detect(bitmap: Bitmap, rotation: Int): String {
        return suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, rotation)
            objectDetector.process(image)
                .addOnSuccessListener {
                    continuation.resume(something(it))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(ObjectDetectionException("Object detection error -> ${it.message}"))
                }
        }
    }

    private fun something(detectedObjects: List<DetectedObject>): String {
        var text = ""
        for (detectedObject in detectedObjects) {
            for (label in detectedObject.labels) {
                text = label.text
            }
        }
        return text
    }

    fun closeDetection() {
        objectDetector.close()
    }

}