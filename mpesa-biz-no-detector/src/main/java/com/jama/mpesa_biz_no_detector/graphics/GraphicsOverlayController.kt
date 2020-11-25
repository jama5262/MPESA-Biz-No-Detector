package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import com.jama.mpesa_biz_no_detector.camera.CameraReticleAnimator
import com.jama.mpesa_biz_no_detector.enums.CameraFlowState
import com.jama.mpesa_biz_no_detector.ui.fragments.CameraViewModel

class GraphicsOverlayController(
    private val cameraViewModel: CameraViewModel,
    private val graphicOverlay: GraphicOverlay
) {

    private val objectConfirmationController = ObjectConfirmationController()
    private val cameraReticleAnimator = CameraReticleAnimator(graphicOverlay)

    private var scaleFactorX = 1.0f
    private var scaleFactorY = 1.0f

    fun start(detectionResult: Triple<Bitmap, Rect, Int>?, imageDimensions: Pair<Float, Float>) {

        scaleFactorY = graphicOverlay.height.toFloat() / imageDimensions.second
        scaleFactorX = graphicOverlay.width.toFloat() / imageDimensions.first

        graphicOverlay.clear()

        if (detectionResult == null) {
            graphicOverlay.add(ObjectReticleGraphic(graphicOverlay, cameraReticleAnimator))
            cameraReticleAnimator.start()
            objectConfirmationController.reset()
            cameraViewModel.setCameraFlowState(CameraFlowState.DETECTING)
        } else {
            if (objectBoxOverlapsConfirmationReticle(detectionResult.second)) {
                // User is confirming the object selection.
                cameraReticleAnimator.cancel()
                graphicOverlay.add(
                    ObjectDetectedGraphic(
                        graphicOverlay,
                        translateRect(detectionResult.second),
                        objectConfirmationController
                    )
                )
                cameraViewModel.confirmingObject(
                    detectionResult.first,
                    objectConfirmationController.progress
                )
                objectConfirmationController.confirming(detectionResult.third)
                if (!objectConfirmationController.isConfirmed) {
                    graphicOverlay.add(
                        ObjectConfirmationGraphic(
                            graphicOverlay,
                            objectConfirmationController
                        )
                    )
                }
            } else {
                // Object is detected but the confirmation reticle is moved off the object box, which
                // indicates user is not trying to pick this object.
                cameraViewModel.setCameraFlowState(CameraFlowState.DETECTED)
                objectConfirmationController.reset()
                graphicOverlay.add(
                    ObjectDetectedGraphic(
                        graphicOverlay,
                        translateRect(detectionResult.second),
                        objectConfirmationController
                    )
                )
                graphicOverlay.add(ObjectReticleGraphic(graphicOverlay, cameraReticleAnimator))
                cameraReticleAnimator.start()
                cameraViewModel.setCameraFlowState(CameraFlowState.DETECTED)
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
        translateX(rect.left.toFloat() - 20f),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat() + 20f),
        translateY(rect.bottom.toFloat())
    )

    private fun translateX(x: Float): Float = x * scaleFactorX
    private fun translateY(y: Float): Float = y * scaleFactorY

}