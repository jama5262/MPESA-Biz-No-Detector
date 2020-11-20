package com.jama.mpesa_business_no_detector.azure_vision_rest

import com.jama.mpesa_business_no_detector.models.Users
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AzureVisionService {

    @POST("analyze")
    suspend fun analyze(
        @Body bytes: RequestBody,
        @Header("Content-Type") contentType: String,
        @Header("Ocp-Apim-Subscription-Key") key: String
    ): Response<String>

    @GET("analyzeResults/{requestId}")
    suspend fun analyzeResults(
        @Header("Ocp-Apim-Subscription-Key") key: String,
        @Path("requestId") requestId: String
    ): Response<String>

    @GET("users")
    suspend fun getTodos(): Response<List<Users>>
}