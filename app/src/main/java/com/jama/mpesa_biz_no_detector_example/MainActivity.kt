package com.jama.mpesa_biz_no_detector_example

import android.Manifest
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private val mpesaBizNoDetector = MPESABizNoDetector(
        Constants.AZURE_VISION_ENDPOINT,
        Constants.AZURE_VISION_KEY
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetStarted.setOnClickListener {
            checkForPermissions()
        }
    }

    private fun startVisionActivity() {
        mpesaBizNoDetector.startActivity(this@MainActivity, CAMERA_REQUEST_CODE)
    }

    private fun checkForPermissions() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    startVisionActivity()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@MainActivity,
                        "Camera permission required",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
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
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val name = data?.getStringExtra("name")
                Log.e("jjj", "called from activity")
                Toast.makeText(this, name, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1
    }
}