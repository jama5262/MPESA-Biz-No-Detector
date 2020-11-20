package com.jama.mpesa_business_no_detector.models

data class VisionResult(
    val status: String,
    val analyzeResult: AnalyzeResult
)

data class AnalyzeResult(
    val readResults: List<ReadResult>
)

data class ReadResult(
    val lines: List<Text>
)

data class Text(
    val text: String
)