package com.jama.mpesa_biz_no_detector.utils

object Constants {
    const val READ_API_ENDPOINT = "/vision/v3.1/read/"
    const val CONTENT_TYPE = "application/octet-stream"

    const val DETECTED_BIZ_NO = "detectedBizNo"
    const val MPESA_BIZ_NO_DETECTOR = "MPESABizNoDetector"

    const val CUTOFF = 70
    val TILL_NO_QUERIES = listOf("buy goods till", "buy goods", "till")
    val PAYBILL_NO_QUERIES = listOf("paybill", "pay bill")
    val ACCOUNT_NO_QUERIES = listOf("account", "acc.", "acc")

    const val BITMAP = "bitmap"
    const val SHARED_ANIM_DURATION = 400L
    const val SHARED_ANIM_DELAY = 400L
    const val TRANSITION_NAME = "imageViewDetected"
}