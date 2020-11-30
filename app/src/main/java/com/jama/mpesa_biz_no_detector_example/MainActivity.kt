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
import androidx.appcompat.app.AlertDialog
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

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        setUpViews()
        loadingDialog = showLoadingDialog()
    }

    private fun setUpViews() {
        buttonVisionActivity.setOnClickListener {
            checkForPermissions {
                startVisionActivity()
            }
        }

        button1.setOnClickListener {
            checkForPermissions {
                detectFromBitmapImage(IMAGE1)
            }
        }

        button2.setOnClickListener {
            checkForPermissions {
                detectFromBitmapImage(IMAGE2)
            }
        }

        button3.setOnClickListener {
            checkForPermissions {
                detectFromBitmapImage(IMAGE3)
            }
        }

        imageView1.apply {
            setImageBitmap(getBitmap(IMAGE1))
        }

        imageView2.apply {
            setImageBitmap(getBitmap(IMAGE2))
        }

        imageView3.apply {
            setImageBitmap(getBitmap(IMAGE3))
        }
    }

    private fun startVisionActivity() {
//        Start the vision activity and let user point at an MPESA sticker or poster
//        to get detect the business number and/or account number
        mpesaBizNoDetector.startActivity(this@MainActivity, CAMERA_REQUEST_CODE)
    }

    private fun detectFromBitmapImage(imageName: String) {
//        User the detect function if you have your own image you would like to
//        detect the business number and/or account number
        lifecycleScope.launch {
            try {
                loadingDialog.show()
                val bitmap = getBitmap(imageName)
                val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
                showDetectedBizNoInfoDialog(detectedBizNo)
            } catch (e: VisionException) {
//                Vision detection failed, request user to try again
                Log.e("jjj", "Vision Error -> ${e.message}")
            } catch (e: BizNoSearchException) {
//                Image detected but could not find a valid MPESA business or account number
//                from them image.
//                Request user to try another image and try again
                Log.e("jjj", "Biz no search error -> ${e.message}")
            } catch (e: Exception) {
//                Unknown error found
                Log.e("jjj", "Error found -> ${e.message}")
            } finally {
                loadingDialog.dismiss()
            }
        }
    }

    private fun showDetectedBizNoInfoDialog(detectedBizNo: DetectedBizNo) {
        val type = "type -> ${detectedBizNo.type}"
        val businessNo = "business no -> ${detectedBizNo.businessNo}"
        val accountNo = "account no -> ${detectedBizNo.accountNo}"

        val message = "$type\n$businessNo\n$accountNo"

        MaterialAlertDialogBuilder(this)
            .setTitle("Detected Business No")
            .setMessage(message)
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoadingDialog(): AlertDialog {
        return MaterialAlertDialogBuilder(this)
            .setMessage("Loading, please wait")
            .setCancelable(false)
            .create()
    }

    private fun checkForPermissions(action: () -> Unit) {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    action()
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

    private fun getBitmap(imageName: String): Bitmap {
        val assetManager: AssetManager = assets
        val stream: InputStream = assetManager.open(imageName)
        val bitmap = BitmapFactory.decodeStream(stream)
        stream.close()
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val detectedBizNo = MPESABizNoDetector.getActivityResult(data!!)
                showDetectedBizNoInfoDialog(detectedBizNo)
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1

        const val IMAGE1 = "image2.jpg"
        const val IMAGE2 = "image5.jpg"
        const val IMAGE3 = "image7.jpg"
    }
}