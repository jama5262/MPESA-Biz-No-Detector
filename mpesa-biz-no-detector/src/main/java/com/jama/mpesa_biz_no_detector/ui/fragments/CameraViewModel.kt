package com.jama.mpesa_biz_no_detector.ui.fragments

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jama.mpesa_biz_no_detector.states.CameraFlowState

class CameraViewModel : ViewModel() {

    private val _cameraFlowState = MutableLiveData<CameraFlowState>(CameraFlowState.Detecting)
    val cameraFlowState = _cameraFlowState

    fun setCameraFlowState(cameraFlowState: CameraFlowState) {
        this._cameraFlowState.value = cameraFlowState
    }

    fun confirmingObject(confirmedBitmap: Bitmap, progress: Float) {
        val isConfirmed = progress.compareTo(1f) == 0
        if (isConfirmed) {
            setCameraFlowState(CameraFlowState.Confirmed(confirmedBitmap))
        } else {
            setCameraFlowState(CameraFlowState.Confirming)
        }
    }
}