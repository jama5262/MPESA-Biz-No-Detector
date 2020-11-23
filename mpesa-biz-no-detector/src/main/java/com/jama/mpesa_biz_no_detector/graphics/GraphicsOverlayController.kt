package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Bitmap
import android.graphics.Rect

class GraphicsOverlayController(private val graphicOverlay: GraphicOverlay) {

    fun start(detectedObject: Pair<Bitmap, Rect>?, imageDimensions: Pair<Float, Float>) {
        graphicOverlay.clear()
        graphicOverlay.add(ObjectReticleGraphic())
        if (detectedObject != null) {
            val boundingBox = detectedObject.second
            graphicOverlay.add(
                ObjectDetectedGraphic(
                    boundingBox,
                    imageDimensions
                )
            )
        }
    }

}