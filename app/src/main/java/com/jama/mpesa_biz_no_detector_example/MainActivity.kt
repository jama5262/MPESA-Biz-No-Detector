package com.jama.mpesa_biz_no_detector_example

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        Constants.AZURE_VISION_ENDPOINT,
        Constants.AZURE_VISION_KEY
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetStarted.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val bitmap = getBitmap() ?: throw Exception("Bitmap Not found")
                    mpesaBizNoDetector.startActivity(this@MainActivity, 1)
//                    mpesaBizNoDetector.detect(bitmap)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val name = data?.getStringExtra("name")
                Log.e("jjj", "called from activity")
                Toast.makeText(this, name, Toast.LENGTH_LONG).show()
            }
        }
    }
}