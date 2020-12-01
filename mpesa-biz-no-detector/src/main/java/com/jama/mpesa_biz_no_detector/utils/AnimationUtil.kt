package com.jama.mpesa_biz_no_detector.utils

import android.view.View
import com.example.android_animation.AndroidAnimation
import com.example.android_animation.enums.Easing

fun animateImageView(
    targetView: View,
    onStart: () -> Unit,
    onEnd: () -> Unit
) {
    AndroidAnimation().apply {
        duration = 1000
        easing = Easing.EXP_OUT
        targetViews(targetView)
        scaleX(0.5f, 1f)
        scaleY(0.5f, 1f)
        onAnimationStart { onStart() }
        onAnimationEnd { onEnd() }
        start()
    }
}

fun animateResults(targetView: View, width: Float) {
    AndroidAnimation().apply {
        duration = 1500
        easing = Easing.EXP_OUT
        targetViews(targetView)
        translateX(width, 0f)
        start()
    }
}