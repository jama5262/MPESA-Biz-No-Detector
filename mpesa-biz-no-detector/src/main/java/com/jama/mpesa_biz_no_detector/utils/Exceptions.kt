package com.jama.mpesa_biz_no_detector.utils

import java.lang.Exception

class ObjectDetectionException(message: String): Exception(message)

class NoObjectDetected(): Exception()