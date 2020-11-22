package com.jama.mpesa_biz_no_detector.utils

fun String.cleanBizNo(): Int {
    val regex = "[^0-9]".toRegex()
    return regex.replace(this, "").toInt()
}