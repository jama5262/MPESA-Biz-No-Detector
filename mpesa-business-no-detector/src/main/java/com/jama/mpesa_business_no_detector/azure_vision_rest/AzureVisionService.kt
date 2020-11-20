package com.jama.mpesa_business_no_detector.azure_vision_rest

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AzureVisionService {

    @Headers("Content-Type: application/octet-stream")
    @POST("read/analyze")
    suspend fun postVisionRequest(
        @Body bytes: RequestBody,
        @Header("Ocp-Apim-Subscription-Key") key: String
    ): Response<String>
}