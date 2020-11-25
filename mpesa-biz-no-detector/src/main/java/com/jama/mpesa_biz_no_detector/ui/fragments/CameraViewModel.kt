package com.jama.mpesa_biz_no_detector.ui.fragments

import android.app.Application
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.enums.WorkflowState

class CameraViewModel(app: Application): AndroidViewModel(app) {

    private val context = app.applicationContext

    private val _confirmedBitmap = MutableLiveData<Bitmap>()
    val confirmedBitmap = _confirmedBitmap

    private val _workflowState = MutableLiveData(WorkflowState.DETECTING)
    val workflowState = _workflowState

    private val _cameraMessage = MutableLiveData(context.getString(R.string.camera_message_1))
    val cameraMessage = _cameraMessage

    fun setWorkflowState(workflowState: WorkflowState) {
        this._workflowState.value = workflowState
    }

    fun confirmingObject(confirmedBitmap: Bitmap, progress: Float) {
        val isConfirmed = progress.compareTo(1f) == 0
        if (isConfirmed) {
            this._confirmedBitmap.value = confirmedBitmap
            setWorkflowState(WorkflowState.CONFIRMED)
        } else {
            setWorkflowState(WorkflowState.CONFIRMING)
        }
    }
}