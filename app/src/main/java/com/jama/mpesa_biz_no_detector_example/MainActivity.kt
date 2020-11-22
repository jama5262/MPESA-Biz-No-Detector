package com.jama.mpesa_biz_no_detector_example

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jama.mpesa_biz_no_detector.MPESABizNoDetector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val mpesaBizNoDetector = MPESABizNoDetector(
        this,
        Constants.AZURE_VISION_KEY,
        Constants.AZURE_VISION_ENDPOINT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetStarted.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val bitmap = getBitmap() ?: throw Exception("Bitmap Not found")
//                    val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
//                    Log.e("jjj", "$detectedBizNo")
                    mpesaBizNoDetector.startActivity()
                } catch (e: Exception) {
                    Log.e("jjj", "Error found -> ${e.message}")
                }
            }
        }

    }

    private fun getBitmap(): Bitmap? {
        val assetManager: AssetManager = assets
        val istr: InputStream = assetManager.open("image.jpg")
        val bitmap = BitmapFactory.decodeStream(istr)
        istr.close()
        return bitmap
    }
}