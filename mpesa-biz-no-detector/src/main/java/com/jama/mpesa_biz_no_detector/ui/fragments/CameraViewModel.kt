package com.jama.mpesa_biz_no_detector.ui.fragments

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jama.mpesa_biz_no_detector.enums.CameraFlowState

class CameraViewModel(app: Application): AndroidViewModel(app) {

    private val context = app.applicationContext

    private val _confirmedBitmap = MutableLiveData<Bitmap>()
    val confirmedBitmap = _confirmedBitmap

    private val _cameraFlowState = MutableLiveData(CameraFlowState.DETECTING)
    val cameraFlowState = _cameraFlowState

    fun setCameraFlowState(cameraFlowState: CameraFlowState) {
        this._cameraFlowState.value = cameraFlowState
    }

    fun confirmingObject(confirmedBitmap: Bitmap, progress: Float) {
        val isConfirmed = progress.compareTo(1f) == 0
        if (isConfirmed) {
            this._confirmedBitmap.value = confirmedBitmap
            setCameraFlowState(CameraFlowState.CONFIRMED)
        } else {
            setCameraFlowState(CameraFlowState.CONFIRMING)
        }
    }
}