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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jama.mpesa_biz_no_detector.MPESABizNoDetector
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.utils.BizNoSearchException
import com.jama.mpesa_biz_no_detector.utils.VisionException
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
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

    private fun detectFromBitmapImage() {
        lifecycleScope.launch {
            try {
                val bitmap = getBitmap()
                val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
                showDetectedBizNoInfo(detectedBizNo)
            } catch (e: VisionException) {
//                Vision detection failed, request user to try again
                Log.e("jjj", "Vision Error -> ${e.message}")
            } catch (e: BizNoSearchException) {
//                Image detected but could not find a valid MPESA business or account number
//                from them image.
//                Request user to move closer and try again
                Log.e("jjj", "Biz no search error -> ${e.message}")
            } catch (e: Exception) {
//                Unknown error found
                Log.e("jjj", "Error found -> ${e.message}")
            }
        }
    }

    private fun showDetectedBizNoInfo(detectedBizNo: DetectedBizNo) {
        val type = "type -> ${detectedBizNo.type}"
        val businessNo = "business no -> ${detectedBizNo.businessNo}"
        val accountNo = "account no -> ${detectedBizNo.accountNo}"

        val message = "$type\n$businessNo\n$accountNo"

        MaterialAlertDialogBuilder(this)
            .setTitle("Detected Business No")
            .setMessage(message)
            .setNeutralButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkForPermissions() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//                    startVisionActivity()
                    detectFromBitmapImage()
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

    private fun getBitmap(): Bitmap {
        val assetManager: AssetManager = assets
        val stream: InputStream = assetManager.open("image.jpg")
        val bitmap = BitmapFactory.decodeStream(stream)
        stream.close()
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val detectedBizNo = MPESABizNoDetector.getActivityResult(data!!)
                showDetectedBizNoInfo(detectedBizNo)
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1
    }
}