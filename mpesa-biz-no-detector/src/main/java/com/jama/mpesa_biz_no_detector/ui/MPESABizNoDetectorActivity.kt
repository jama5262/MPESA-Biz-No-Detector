package com.jama.mpesa_biz_no_detector.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jama.mpesa_biz_no_detector.R

class MPESABizNoDetectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_biz_no_detector)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("name", "Jama Mohamed")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}