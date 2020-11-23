package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class ObjectReticleGraphic: Graphic {

    private val paint = Paint()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            color = Color.RED
        }
    }

    override fun draw(canvas: Canvas) {
        val cx = (canvas.width / 2).toFloat()
        val cy = (canvas.height / 2).toFloat()
        canvas.drawCircle(cx, cy, 50f, paint)
    }
}