package com.jama.mpesa_biz_no_detector.azureVisionRest

import com.jama.mpesa_biz_no_detector.models.VisionResult
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AzureVisionService {

    @POST("analyze")
    suspend fun analyze(
        @Body bytes: RequestBody,
        @Header("Content-Type") contentType: String,
        @Header("Ocp-Apim-Subscription-Key") key: String
    ): Response<Void>

    @GET("analyzeResults/{requestId}")
    suspend fun analyzeResults(
        @Header("Ocp-Apim-Subscription-Key") key: String,
        @Path("requestId") requestId: String
    ): Response<VisionResult>
}