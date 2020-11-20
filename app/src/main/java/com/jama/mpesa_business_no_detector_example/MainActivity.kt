package com.jama.mpesa_business_no_detector_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jama.mpesa_business_no_detector.MPESABizNoDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mpesaBizNoDetector = MPESABizNoDetector(
        Constants.AZURE_VISION_KEY,
        Constants.AZURE_VISION_ENDPOINT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetStarted.setOnClickListener {
            mpesaBizNoDetector.runRestApi()
        }

    }
}