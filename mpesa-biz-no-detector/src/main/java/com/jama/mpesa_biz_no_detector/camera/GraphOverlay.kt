package com.jama.mpesa_biz_no_detector.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GraphOverlay: View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet? = null) : super(context, attr)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var detectionOverlay = RectF()
    private val reticleOverlay = Rect()
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

    fun updateRect(rect: Rect, image: Pair<Int, Int>) {
        scaleFactorY = height.toFloat() / image.second
        scaleFactorX = width.toFloat() / image.first

        detectionOverlay = translateRect(rect)
        val centerWidth = width / 2
        val centerHeight = height / 2
        reticleOverlay.apply {
            left = centerWidth - 20
            top = centerHeight - 20
            right = centerWidth + 20
            bottom = centerHeight + 20
        }
        postInvalidate()
    }

    private fun translateRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    private fun translateX(x: Float): Float = x * scaleFactorX
    private fun translateY(y: Float): Float = y * scaleFactorY

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(detectionOverlay, paint)
        canvas?.drawRect(reticleOverlay, paint)
    }

}