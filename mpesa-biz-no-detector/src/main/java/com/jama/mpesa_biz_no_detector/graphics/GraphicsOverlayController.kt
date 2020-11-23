package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF

class GraphicsOverlayController(private val graphicOverlay: GraphicOverlay) {

    private var scaleFactorX = 1.0f
    private var scaleFactorY = 1.0f

    fun start(detectionResult: Pair<Bitmap, Rect>?, imageDimensions: Pair<Float, Float>) {

        scaleFactorY = graphicOverlay.height.toFloat() / imageDimensions.second
        scaleFactorX = graphicOverlay.width.toFloat() / imageDimensions.first

        graphicOverlay.clear()

        if (detectionResult == null) {
            graphicOverlay.add(ObjectReticleGraphic())
        } else {
            if (objectBoxOverlapsConfirmationReticle(detectionResult.second)) {
                // User is confirming the object selection.
                graphicOverlay.add(
                    ObjectDetectedGraphic(translateRect(detectionResult.second))
                )
                graphicOverlay.add(ObjectConfirmationGraphic())
            } else {
                // Object is detected but the confirmation reticle is moved off the object box, which
                // indicates user is not trying to pick this object.
                graphicOverlay.add(
                    ObjectDetectedGraphic(translateRect(detectionResult.second))
                )
                graphicOverlay.add(ObjectReticleGraphic())
            }
        }
    }

    private fun objectBoxOverlapsConfirmationReticle(detectedObject: Rect): Boolean {
        val boxRect = translateRect(detectedObject)
        val reticleCenterX = graphicOverlay.width / 2f
        val reticleCenterY = graphicOverlay.height / 2f
        val reticleRect = RectF(
            reticleCenterX - 50f,
            reticleCenterY - 50f,
            reticleCenterX + 50f,
            reticleCenterY + 50f
        )
        return reticleRect.intersect(boxRect)
    }

    private fun translateRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    private fun translateX(x: Float): Float = x * scaleFactorX
    private fun translateY(y: Float): Float = y * scaleFactorY

}