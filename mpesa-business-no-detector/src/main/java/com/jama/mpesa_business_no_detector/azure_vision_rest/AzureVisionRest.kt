package com.jama.mpesa_business_no_detector.azure_vision_rest

import android.util.Log
import com.jama.mpesa_business_no_detector.utils.Constants
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception

class AzureVisionRest(
    baseUrl: String,
    private val azureVisionKey: String,
    private val byteArray: ByteArray
) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val azureVisionService = retrofit.create(AzureVisionService::class.java)

    suspend fun startVision() {
        try {
            Log.e("jjj", "Starting to analyze")
            val analyzedResponse = analyze()
            val requestId = analyzedResponse.headers().get("apim-request-id")
            Log.e("jjj", "$requestId")
        } catch (e: Exception) {
            Log.e("jjj", "Error -> ${e.message}")
        }
    }

    private suspend fun analyze(): Response<String> {
        val contentType = Constants.CONTENT_TYPE
        val requestBody = RequestBody.create(
            MediaType.parse(contentType),
            byteArray
        )
        return azureVisionService.analyze(requestBody, contentType, azureVisionKey)
    }
}