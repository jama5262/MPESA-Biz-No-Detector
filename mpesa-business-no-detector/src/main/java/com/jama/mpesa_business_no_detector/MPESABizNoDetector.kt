package com.jama.mpesa_business_no_detector

import android.graphics.Bitmap
import android.util.Log
import com.jama.mpesa_business_no_detector.azure_vision_rest.AzureVisionRest
import com.jama.mpesa_business_no_detector.utils.Constants
import com.jama.mpesa_business_no_detector.utils.toByteArray

class MPESABizNoDetector(
    private val azureVisionKey: String,
    azureVisionEndPoint: String
) {

    private val baseUrl = "$azureVisionEndPoint${Constants.READ_API_ENDPOINT}"
    private val baseUrl2 = "https://jsonplaceholder.typicode.com/"

    suspend fun detect(bitmap: Bitmap) {
        val byteArray = bitmap.toByteArray()
        val azureVisionRest = AzureVisionRest(baseUrl, azureVisionKey, byteArray)
        azureVisionRest.startVision()
    }

}