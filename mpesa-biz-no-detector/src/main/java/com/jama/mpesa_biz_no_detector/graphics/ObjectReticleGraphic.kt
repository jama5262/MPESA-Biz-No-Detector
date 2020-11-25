package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.jama.mpesa_biz_no_detector.R
import com.jama.mpesa_biz_no_detector.camera.CameraReticleAnimator

class ObjectReticleGraphic(
    graphicOverlay: GraphicOverlay,
    private val cameraReticleAnimator: CameraReticleAnimator
) : Graphic {

    private val outerRingFillPaint: Paint
    private val outerRingStrokePaint: Paint
    private val innerRingStrokePaint: Paint
    private val ripplePaint: Paint
    private val outerRingFillRadius: Int
    private val outerRingStrokeRadius: Int
    private val innerRingStrokeRadius: Int
    private val rippleSizeOffset: Int
    private val rippleStrokeWidth: Int
    private val rippleAlpha: Int

    init {
        val resources = graphicOverlay.resources
        val context = graphicOverlay.context
        outerRingFillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.object_reticle_outer_ring_fill)
        }

        outerRingStrokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_stroke_width).toFloat()
            strokeCap = Paint.Cap.ROUND
            color = ContextCompat.getColor(context, R.color.object_reticle_outer_ring_stroke)
        }

        innerRingStrokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimensionPixelOffset(R.dimen.object_reticle_inner_ring_stroke_width).toFloat()
            strokeCap = Paint.Cap.ROUND
            color = ContextCompat.getColor(context, R.color.colorOnPrimary)
        }

        ripplePaint = Paint().apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.reticle_ripple)
        }

        outerRingFillRadius = resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_fill_radius)
        outerRingStrokeRadius = resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_stroke_radius)
        innerRingStrokeRadius = resources.getDimensionPixelOffset(R.dimen.object_reticle_inner_ring_stroke_radius)
        rippleSizeOffset = resources.getDimensionPixelOffset(R.dimen.object_reticle_ripple_size_offset)
        rippleStrokeWidth = resources.getDimensionPixelOffset(R.dimen.object_reticle_ripple_stroke_width)
        rippleAlpha = ripplePaint.alpha
    }

    override fun draw(canvas: Canvas) {
        val cx = (canvas.width / 2).toFloat()
        val cy = (canvas.height / 2).toFloat()

        canvas.drawCircle(cx, cy, outerRingFillRadius.toFloat(), outerRingFillPaint)
        canvas.drawCircle(cx, cy, outerRingStrokeRadius.toFloat(), outerRingStrokePaint)
        canvas.drawCircle(cx, cy, innerRingStrokeRadius.toFloat(), innerRingStrokePaint)

        ripplePaint.alpha = (rippleAlpha * cameraReticleAnimator.rippleAlphaScale).toInt()
        ripplePaint.strokeWidth = rippleStrokeWidth * cameraReticleAnimator.rippleStrokeWidthScale
        val radius = outerRingStrokeRadius + rippleSizeOffset * cameraReticleAnimator.rippleSizeScale
        canvas.drawCircle(cx, cy, radius, ripplePaint)
    }
}