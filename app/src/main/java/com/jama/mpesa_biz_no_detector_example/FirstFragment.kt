package com.jama.mpesa_biz_no_detector_example

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jama.mpesa_biz_no_detector.MPESABizNoDetector
import kotlinx.android.synthetic.main.fragment_first.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception

class FirstFragment : Fragment() {

    private lateinit var rootView: View

    private val mpesaBizNoDetector = MPESABizNoDetector(
        Constants.AZURE_VISION_ENDPOINT,
        Constants.AZURE_VISION_KEY
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_first, container, false)

        rootView.buttonProceed.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val bitmap = getBitmap() ?: throw Exception("Bitmap Not found")
                    mpesaBizNoDetector.startActivity(this@FirstFragment, 2)
                } catch (e: Exception) {
                    Log.e("jjj", "Error found -> ${e.message}")
                }
            }
        }

        return rootView
    }

    private fun getBitmap(): Bitmap? {
        val assetManager: AssetManager = requireActivity().assets
        val istr: InputStream = assetManager.open("image.jpg")
        val bitmap = BitmapFactory.decodeStream(istr)
        istr.close()
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val detectedBizNo = MPESABizNoDetector.getActivityResult(data!!)
                Log.e("jjj", "$detectedBizNo")
            }
        }
    }
}