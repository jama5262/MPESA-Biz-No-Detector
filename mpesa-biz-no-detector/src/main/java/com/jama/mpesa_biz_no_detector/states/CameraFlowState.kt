package com.jama.mpesa_biz_no_detector.states

import android.graphics.Bitmap

sealed class CameraFlowState {
    object Detecting: CameraFlowState()
    object Detected: CameraFlowState()
    object Confirming: CameraFlowState()
    data class Confirmed(val bitmap: Bitmap): CameraFlowState()
}