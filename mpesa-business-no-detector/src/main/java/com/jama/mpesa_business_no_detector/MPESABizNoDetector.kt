package com.jama.mpesa_business_no_detector

import android.graphics.Bitmap
import com.jama.mpesa_business_no_detector.azureVisionRest.AzureVisionRest
import com.jama.mpesa_business_no_detector.fuzzySearch.SearchBizNo
import com.jama.mpesa_business_no_detector.models.VisionResult
import com.jama.mpesa_business_no_detector.utils.Constants
import com.jama.mpesa_business_no_detector.utils.toByteArray

class MPESABizNoDetector(
    private val azureVisionKey: String,
    azureVisionEndPoint: String
) {

    private val baseUrl = "$azureVisionEndPoint${Constants.READ_API_ENDPOINT}"

    suspend fun detect(bitmap: Bitmap) {
        performFuzzySearch()
    }

    private suspend fun getVisionResult(bitmap: Bitmap): VisionResult {
        val byteArray = bitmap.toByteArray()
        val azureVisionRest = AzureVisionRest(baseUrl, azureVisionKey, byteArray)
        return azureVisionRest.startVision() ?: throw Exception("Failed, please try again")
    }

    private fun performFuzzySearch() {
        val searchBizNo = SearchBizNo().search()
    }

}