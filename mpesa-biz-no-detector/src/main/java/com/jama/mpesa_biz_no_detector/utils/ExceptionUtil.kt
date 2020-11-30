package com.jama.mpesa_biz_no_detector.utils

class VisionException(val errorMessage: String): Exception(errorMessage)

class BizNoSearchException(val errorMessage: String): Exception(errorMessage)