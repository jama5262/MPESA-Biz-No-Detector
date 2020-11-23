package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.*

class ObjectDetectedGraphic(private val boundingBox: RectF): Graphic {

    private val paint = Paint()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            color = Color.RED
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(boundingBox, paint)
    }
}