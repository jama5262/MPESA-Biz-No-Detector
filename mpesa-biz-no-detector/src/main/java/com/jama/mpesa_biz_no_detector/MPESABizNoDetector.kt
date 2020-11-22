package com.jama.mpesa_biz_no_detector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.jama.mpesa_biz_no_detector.azureVisionRest.AzureVisionRest
import com.jama.mpesa_biz_no_detector.fuzzySearch.SearchBizNo
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.models.VisionResult
import com.jama.mpesa_biz_no_detector.ui.MPESABizNoDetectorActivity
import com.jama.mpesa_biz_no_detector.utils.Constants
import com.jama.mpesa_biz_no_detector.utils.toByteArray

class MPESABizNoDetector(
    private val activity: Activity,
    private val azureVisionKey: String,
    azureVisionEndPoint: String
) {

    private val baseUrl = "$azureVisionEndPoint${Constants.READ_API_ENDPOINT}"

    fun startActivity() {
        val intent = Intent(activity, MPESABizNoDetectorActivity::class.java)
        activity.startActivity(intent)
    }

    suspend fun detect(bitmap: Bitmap): DetectedBizNo {
        val visionResult = getVisionResult(bitmap)
        val visionReadResults = visionResult.analyzeResult.readResults[0]
        val choices = visionReadResults.lines.map { it.text }
        return performFuzzySearch(choices)
    }

    private suspend fun getVisionResult(bitmap: Bitmap): VisionResult {
        val byteArray = bitmap.toByteArray()
        val azureVisionRest = AzureVisionRest(baseUrl, azureVisionKey, byteArray)
        return azureVisionRest.startVision() ?: throw Exception("Vision result not found")
    }

    private fun performFuzzySearch(choices: List<String>): DetectedBizNo {
        return SearchBizNo(choices).search() ?: throw Exception("Detected Biz No not found")
    }

}