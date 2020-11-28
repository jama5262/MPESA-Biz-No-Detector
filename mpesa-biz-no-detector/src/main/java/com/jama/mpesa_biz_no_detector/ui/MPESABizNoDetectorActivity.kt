package com.jama.mpesa_biz_no_detector.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.jama.mpesa_biz_no_detector.MPESABizNoDetector
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.utils.Constants

class MPESABizNoDetectorActivity : AppCompatActivity() {

    lateinit var mpesaBizNoDetector: MPESABizNoDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_biz_no_detector)
        initialize()
    }

    private fun initialize() {
        mpesaBizNoDetector =
            intent.getSerializableExtra(Constants.MPESA_BIZ_NO_DETECTOR) as MPESABizNoDetector
    }

    fun sendResults(detectedBizNo: DetectedBizNo) {
        val intent = Intent()
        val bundle = bundleOf(Constants.DETECTED_BIZ_NO to detectedBizNo)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}