package com.jama.mpesa_biz_no_detector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jama.mpesa_biz_no_detector.azureVisionRest.AzureVisionRest
import com.jama.mpesa_biz_no_detector.fuzzySearch.SearchBizNo
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.models.VisionResult
import com.jama.mpesa_biz_no_detector.ui.MPESABizNoDetectorActivity
import com.jama.mpesa_biz_no_detector.utils.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class MPESABizNoDetector(
    private val azureVisionEndPoint: String,
    private val azureVisionKey: String
) : Serializable {

    private val activityClass = MPESABizNoDetectorActivity::class.java

    fun start(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, activityClass)
        val bundle = bundleOf(
            "MPESABizNoDetector" to this
        )
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestCode)
    }

    fun start(fragment: Fragment, requestCode: Int) {
        val intent = Intent(fragment.context, activityClass)
        val bundle = bundleOf(
            "MPESABizNoDetector" to this
        )
        intent.putExtras(bundle)
        fragment.startActivityForResult(intent, requestCode)
    }

    suspend fun detect(bitmap: Bitmap): DetectedBizNo {
        val visionResult = withContext(Dispatchers.IO) { getVisionResult(bitmap) }
        val visionReadResults = visionResult.analyzeResult.readResults[0]
        val choices = visionReadResults.lines.map { it.text }
        return performFuzzySearch(choices)
    }

    private suspend fun getVisionResult(bitmap: Bitmap): VisionResult {
        val byteArray = bitmap.toByteArray()
        val azureVisionRest = AzureVisionRest(
            azureVisionEndPoint,
            azureVisionKey,
            byteArray
        )
        return azureVisionRest.startVision() ?: throw Exception("Vision result not found")
    }

    private fun performFuzzySearch(choices: List<String>): DetectedBizNo {
        return SearchBizNo(choices).search() ?: throw Exception("Detected Biz No not found")
    }

    companion object {
        fun getActivityResult(data: Intent): DetectedBizNo {
            return data.getSerializableExtra("detectedBizNo") as DetectedBizNo
        }
    }

}