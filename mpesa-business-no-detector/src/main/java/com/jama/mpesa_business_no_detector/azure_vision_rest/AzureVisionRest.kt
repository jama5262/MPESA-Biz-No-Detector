package com.jama.mpesa_business_no_detector.azure_vision_rest

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
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

    suspend fun startVision() {
        try {
            val postResponse = sendPostRequest()
            val headers = postResponse.headers()
        } catch (e: Exception) {
            Log.e("jjj", "Error -> ${e.message}")
        }
    }

    private suspend fun sendPostRequest(): Response<String> {
        val azureVisionService = retrofit.create(AzureVisionService::class.java)
        val requestBody = RequestBody.create(
            MediaType.parse("application/octet-stream"),
            byteArray
        )
        return azureVisionService.postVisionRequest(requestBody, azureVisionKey)
    }
}