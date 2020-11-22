package com.jama.mpesa_biz_no_detector.utils

fun String.cleanBizNo(): Int {
    val regex = "[^0-9]".toRegex()
    val replacedRegex = regex.replace(this, "")
    if (replacedRegex.isBlank()) return 0
    return replacedRegex.toInt()
}