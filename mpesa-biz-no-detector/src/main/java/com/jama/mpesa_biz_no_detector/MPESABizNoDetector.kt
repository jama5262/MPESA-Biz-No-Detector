package com.jama.mpesa_biz_no_detector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jama.mpesa_biz_no_detector.azureVisionRest.AzureVisionRest
import com.jama.mpesa_biz_no_detector.fuzzySearch.SearchBizNo
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.models.VisionResult
import com.jama.mpesa_biz_no_detector.ui.MPESABizNoDetectorActivity
import com.jama.mpesa_biz_no_detector.utils.BizNoSearchException
import com.jama.mpesa_biz_no_detector.utils.Constants
import com.jama.mpesa_biz_no_detector.utils.VisionException
import com.jama.mpesa_biz_no_detector.utils.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class MPESABizNoDetector(
    private val azureVisionEndPoint: String,
    private val azureVisionKey: String
) : Serializable {

    /**
     * Starts vision activity from activity class
     *
     * @param activity the activity class
     * @param requestCode the request code
     */
    fun startActivity(activity: Activity, requestCode: Int) {
        val intent = getIntent(activity)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Starts vision activity from fragment class
     *
     * @param fragment the fragment class
     * @param requestCode the request code
     */
    fun startActivity(fragment: Fragment, requestCode: Int) {
        val intent = getIntent(fragment.requireContext())
        fragment.startActivityForResult(intent, requestCode)
    }

    /**
     * Starts azure vision detection and return a DetectedBizNo.
     *
     * @param bitmap the bitmap image
     */
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
        return azureVisionRest.startVision()
            ?: throw VisionException("Could not find vision result from the image")
    }

    private fun performFuzzySearch(choices: List<String>): DetectedBizNo {
        return SearchBizNo(choices).search()
            ?: throw BizNoSearchException("Could not find business number in the image")
    }

    private fun getIntent(context: Context): Intent {
        val intent = Intent(context, ACTIVITY_CLASS)
        val bundle = bundleOf(
            Constants.MPESA_BIZ_NO_DETECTOR to this
        )
        intent.putExtras(bundle)
        return intent
    }

    companion object {
        private val ACTIVITY_CLASS = MPESABizNoDetectorActivity::class.java

        fun getActivityResult(data: Intent): DetectedBizNo {
            return data.getSerializableExtra(Constants.DETECTED_BIZ_NO) as DetectedBizNo
        }
    }

}