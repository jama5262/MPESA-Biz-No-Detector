package com.jama.mpesa_biz_no_detector.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jama.mpesa_biz_no_detector.MPESABizNoDetector
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo

class MPESABizNoDetectorActivity : AppCompatActivity() {

    lateinit var mpesaBizNoDetector: MPESABizNoDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_biz_no_detector)
        mpesaBizNoDetector = intent.getSerializableExtra("MPESABizNoDetector") as MPESABizNoDetector
    }

    fun sendResults(detectedBizNo: DetectedBizNo) {
        val intent = Intent()
        intent.putExtra("name", "Jama Mohamed")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

//    override fun onBackPressed() {
//        val intent = Intent()
//        intent.putExtra("name", "Jama Mohamed")
//        setResult(Activity.RESULT_OK, intent)
//        finish()
//    }

}