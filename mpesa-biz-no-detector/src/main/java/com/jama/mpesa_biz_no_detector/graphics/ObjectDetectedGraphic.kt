package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.*

class ObjectDetectedGraphic(
    private val boundingBox: Rect,
    private val imageDimensions: Pair<Float, Float>
): Graphic {

    private val paint = Paint()

    private var scaleFactorX = 1.0f
    private var scaleFactorY = 1.0f

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            color = Color.RED
        }
    }

    override fun draw(canvas: Canvas) {
        scaleFactorY = canvas.height.toFloat() / imageDimensions.second
        scaleFactorX = canvas.width.toFloat() / imageDimensions.first

        val rect = translateRect(boundingBox)
        canvas.drawRect(rect, paint)
    }

    private fun translateRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    private fun translateX(x: Float): Float = x * scaleFactorX
    private fun translateY(y: Float): Float = y * scaleFactorY
}