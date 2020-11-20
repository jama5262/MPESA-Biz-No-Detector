package com.jama.mpesa_business_no_detector.models

data class VisionResult(
    val status: String,
    val analyzeResult: AnalyzeResult
)

data class AnalyzeResult(
    val version: String,
    val readResults: List<ReadResult>
)

data class ReadResult(
    val page: Int,
    val lines: List<Text>
)

data class Text(
    val text: String
)