package com.jama.mpesa_biz_no_detector.states

import com.jama.mpesa_biz_no_detector.models.DetectedBizNo

sealed class ResultsState {
    data class Success(val detectedBizNo: DetectedBizNo): ResultsState()
    object Fail: ResultsState()
}