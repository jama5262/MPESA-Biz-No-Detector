package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class ObjectConfirmationGraphic(private val confirmationController: ObjectConfirmationController) : Graphic {
    private val paint = Paint()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            color = Color.GREEN
        }
    }

    override fun draw(canvas: Canvas) {
        val cx = (canvas.width / 2).toFloat()
        val cy = (canvas.height / 2).toFloat()

        val progressRect = RectF(
            cx - 50f,
            cy - 50f,
            cx + 50f,
            cy + 50f
        )

        val sweepAngle = confirmationController.progress * 360f

        canvas.drawArc(progressRect, 0f, sweepAngle, false, paint)
    }
}