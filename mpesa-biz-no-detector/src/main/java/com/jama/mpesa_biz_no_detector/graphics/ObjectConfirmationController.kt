package com.jama.mpesa_biz_no_detector.graphics

import android.os.CountDownTimer

class ObjectConfirmationController {

    private val countDownTimer: CountDownTimer
    private var objectId: Int? = null
    var progress = 0f
    val isConfirmed: Boolean = progress.compareTo(1f) == 0

    init {
        val confirmationTimeMs = 1500L
        countDownTimer = object : CountDownTimer(confirmationTimeMs, 20) {
            override fun onFinish() {
                progress = 1f
            }

            override fun onTick(millisUntilFinished: Long) {
                progress = (confirmationTimeMs - millisUntilFinished).toFloat() / confirmationTimeMs
            }
        }
    }

    fun confirming(objectId: Int?) {
        if (objectId == this.objectId) return
        reset()
        this.objectId = objectId
        countDownTimer.start()
    }

    fun reset() {
        countDownTimer.cancel()
        objectId = null
        progress = 0f
    }

}