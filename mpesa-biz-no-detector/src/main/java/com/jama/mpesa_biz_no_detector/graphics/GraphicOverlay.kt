package com.jama.mpesa_biz_no_detector.graphics

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GraphicOverlay : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet? = null) : super(context, attr)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private val graphics = mutableListOf<Graphic>()

    fun add(graphic: Graphic) {
        graphics.add(graphic)
    }

    fun clear() {
        graphics.clear()
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        graphics.forEach {
            it.draw(canvas)
        }
    }
}