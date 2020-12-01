package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.jama.mpesa_biz_no_detector.R

class ObjectConfirmationGraphic(
    graphicOverlay: GraphicOverlay,
    private val confirmationController: ObjectConfirmationController
) : Graphic {

    private val outerRingFillPaint: Paint
    private val outerRingStrokePaint: Paint
    private val innerRingPaint: Paint
    private val progressRingStrokePaint: Paint
    private val outerRingFillRadius: Int
    private val outerRingStrokeRadius: Int
    private val innerRingStrokeRadius: Int

    init {
        val resources = graphicOverlay.resources
        val context = graphicOverlay.context
        outerRingFillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.object_reticle_outer_ring_fill)
        }

        outerRingStrokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth =
                resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_stroke_width)
                    .toFloat()
            strokeCap = Paint.Cap.ROUND
            color = ContextCompat.getColor(context, R.color.object_reticle_outer_ring_stroke)
        }

        progressRingStrokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth =
                resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_stroke_width)
                    .toFloat()
            strokeCap = Paint.Cap.ROUND
            color = ContextCompat.getColor(context, R.color.colorPrimaryLight)
        }

        innerRingPaint = Paint()
        innerRingPaint.style = Paint.Style.STROKE
        innerRingPaint.strokeWidth =
            resources.getDimensionPixelOffset(R.dimen.object_reticle_inner_ring_stroke_width)
                .toFloat()
        innerRingPaint.strokeCap = Paint.Cap.ROUND
        innerRingPaint.color = ContextCompat.getColor(context, R.color.colorPrimaryLight)

        outerRingFillRadius =
            resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_fill_radius)
        outerRingStrokeRadius =
            resources.getDimensionPixelOffset(R.dimen.object_reticle_outer_ring_stroke_radius)
        innerRingStrokeRadius =
            resources.getDimensionPixelOffset(R.dimen.object_reticle_inner_ring_stroke_radius)
    }

    override fun draw(canvas: Canvas) {
        val cx = (canvas.width / 2).toFloat()
        val cy = (canvas.height / 2).toFloat()

        canvas.drawCircle(cx, cy, outerRingFillRadius.toFloat(), outerRingFillPaint)
        canvas.drawCircle(cx, cy, outerRingStrokeRadius.toFloat(), outerRingStrokePaint)
        canvas.drawCircle(cx, cy, innerRingStrokeRadius.toFloat(), innerRingPaint)

        val progressRect = RectF(
            cx - outerRingStrokeRadius,
            cy - outerRingStrokeRadius,
            cx + outerRingStrokeRadius,
            cy + outerRingStrokeRadius
        )
        val sweepAngle = confirmationController.progress * 360
        canvas.drawArc(
            progressRect,
            0f,
            sweepAngle,
            false,
            progressRingStrokePaint
        )
    }
}