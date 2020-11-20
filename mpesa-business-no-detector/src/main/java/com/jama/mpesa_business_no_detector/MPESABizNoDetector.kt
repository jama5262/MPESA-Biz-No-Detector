package com.jama.mpesa_business_no_detector

import android.util.Log

class MPESABizNoDetector(
    private val azureVisionKey: String,
    private val azureVisionEndPoint: String
) {

    fun runRestApi() {
        Log.e("jjj", "Key -> $azureVisionKey and endpoint -> $azureVisionEndPoint")
    }

}