package com.jama.mpesa_biz_no_detector.camera

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.jama.mpesa_biz_no_detector.graphics.GraphicOverlay

class CameraReticleAnimator(private val graphicOverlay: GraphicOverlay) {

    var rippleAlphaScale = 0f
        private set

    var rippleSizeScale = 0f
        private set

    var rippleStrokeWidthScale = 1f
        private set

    private val animatorSet: AnimatorSet

    init {
        val rippleFadeInAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION_RIPPLE_FADE_IN_MS)
        rippleFadeInAnimator.addUpdateListener { animation ->
            rippleAlphaScale = animation.animatedValue as Float
            graphicOverlay.postInvalidate()
        }

        val rippleFadeOutAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(DURATION_RIPPLE_FADE_OUT_MS)
        rippleFadeOutAnimator.startDelay = START_DELAY_RIPPLE_FADE_OUT_MS
        rippleFadeOutAnimator.addUpdateListener { animation ->
            rippleAlphaScale = animation.animatedValue as Float
            graphicOverlay.postInvalidate()
        }

        val rippleExpandAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION_RIPPLE_EXPAND_MS)
        rippleExpandAnimator.startDelay = START_DELAY_RIPPLE_EXPAND_MS
        rippleExpandAnimator.interpolator = FastOutSlowInInterpolator()
        rippleExpandAnimator.addUpdateListener { animation ->
            rippleSizeScale = animation.animatedValue as Float
            graphicOverlay.postInvalidate()
        }

        val rippleStrokeWidthShrinkAnimator =
            ValueAnimator.ofFloat(1f, 0.5f).setDuration(DURATION_RIPPLE_STROKE_WIDTH_SHRINK_MS)
        rippleStrokeWidthShrinkAnimator.startDelay = START_DELAY_RIPPLE_STROKE_WIDTH_SHRINK_MS
        rippleStrokeWidthShrinkAnimator.interpolator = FastOutSlowInInterpolator()
        rippleStrokeWidthShrinkAnimator.addUpdateListener { animation ->
            rippleStrokeWidthScale = animation.animatedValue as Float
            graphicOverlay.postInvalidate()
        }

        val fakeAnimatorForRestartDelay = ValueAnimator.ofInt(0, 0).setDuration(DURATION_RESTART_DORMANCY_MS)
        fakeAnimatorForRestartDelay.startDelay = START_DELAY_RESTART_DORMANCY_MS
        animatorSet = AnimatorSet()
        animatorSet.playTogether(
            rippleFadeInAnimator,
            rippleFadeOutAnimator,
            rippleExpandAnimator,
            rippleStrokeWidthShrinkAnimator,
            fakeAnimatorForRestartDelay
        )
    }

    fun start() {
        if (!animatorSet.isRunning) animatorSet.start()
    }

    fun cancel() {
        animatorSet.cancel()
        rippleAlphaScale = 0f
        rippleSizeScale = 0f
        rippleStrokeWidthScale = 1f
    }

    companion object {
        private const val DURATION_RIPPLE_FADE_IN_MS: Long = 333
        private const val DURATION_RIPPLE_FADE_OUT_MS: Long = 500
        private const val DURATION_RIPPLE_EXPAND_MS: Long = 833
        private const val DURATION_RIPPLE_STROKE_WIDTH_SHRINK_MS: Long = 833
        private const val DURATION_RESTART_DORMANCY_MS: Long = 1333
        private const val START_DELAY_RIPPLE_FADE_OUT_MS: Long = 667
        private const val START_DELAY_RIPPLE_EXPAND_MS: Long = 333
        private const val START_DELAY_RIPPLE_STROKE_WIDTH_SHRINK_MS: Long = 333
        private const val START_DELAY_RESTART_DORMANCY_MS: Long = 1167
    }
}