package com.jama.mpesa_biz_no_detector.objectDetection

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jama.mpesa_biz_no_detector.utils.resize
import com.jama.mpesa_biz_no_detector.utils.rotate
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ObjectDetection {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .build()

    private val objectDetector = ObjectDetection.getClient(options)

    suspend fun detect(bitmap: Bitmap, rotation: Int): Triple<Bitmap, Rect, Int>? {
        return suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, rotation)
            objectDetector.process(image)
                .addOnSuccessListener {
                    val detectionResult = getDetectedResult(bitmap, rotation, it)
                    continuation.resume(detectionResult)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(Exception("Object detection error -> ${it.message}"))
                }
        }
    }

    private fun getDetectedResult(
        bitmap: Bitmap,
        rotation: Int,
        detectedObjects: List<DetectedObject>
    ): Triple<Bitmap, Rect, Int>? {
        var boundingBox = Rect()
        var detectedBitmap: Bitmap? = null
        var trackingId = 0
        for (detectedObject in detectedObjects) {
            boundingBox = detectedObject.boundingBox
            detectedBitmap = bitmap.rotate(rotation).resize(boundingBox)
            trackingId = detectedObject.trackingId!!
        }
        if (detectedBitmap == null) return null
        return Triple(detectedBitmap, boundingBox, trackingId)
    }
}