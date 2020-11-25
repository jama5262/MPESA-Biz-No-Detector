package com.jama.mpesa_biz_no_detector.graphics

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.jama.mpesa_biz_no_detector.R

class ObjectDetectedGraphic(
    graphicOverlay: GraphicOverlay,
    private val boundingBox: RectF,
    private val objectConfirmationController: ObjectConfirmationController
) : Graphic {

    private val scrimPaint: Paint = Paint()
    private val eraserPaint: Paint
    private val boxPaint: Paint

    @ColorInt
    private val boxGradientStartColor: Int

    @ColorInt
    private val boxGradientEndColor: Int
    private val boxCornerRadius: Int

    init {
        val context = graphicOverlay.context
        scrimPaint.shader = if (objectConfirmationController.isConfirmed) {
            LinearGradient(
                0f,
                0f,
                graphicOverlay.width.toFloat(),
                graphicOverlay.height.toFloat(),
                ContextCompat.getColor(context, R.color.object_confirmed_bg_gradient_start),
                ContextCompat.getColor(context, R.color.object_confirmed_bg_gradient_end),
                Shader.TileMode.CLAMP
            )
        } else {
            LinearGradient(
                0f,
                0f,
                graphicOverlay.width.toFloat(),
                graphicOverlay.height.toFloat(),
                ContextCompat.getColor(context, R.color.object_detected_bg_gradient_start),
                ContextCompat.getColor(context, R.color.object_detected_bg_gradient_end),
                Shader.TileMode.CLAMP
            )
        }

        eraserPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        boxPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = context
                .resources
                .getDimensionPixelOffset(
                    if (objectConfirmationController.isConfirmed) {
                        R.dimen.bounding_box_confirmed_stroke_width
                    } else {
                        R.dimen.bounding_box_stroke_width
                    }
                ).toFloat()
            color = Color.WHITE
        }

        boxGradientStartColor = ContextCompat.getColor(context, R.color.bounding_box_gradient_start)
        boxGradientEndColor = ContextCompat.getColor(context, R.color.bounding_box_gradient_end)
        boxCornerRadius =
            context.resources.getDimensionPixelOffset(R.dimen.bounding_box_corner_radius)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), scrimPaint)
        canvas.drawRoundRect(
            boundingBox,
            boxCornerRadius.toFloat(),
            boxCornerRadius.toFloat(),
            eraserPaint
        )

        boxPaint.shader = if (objectConfirmationController.isConfirmed) null
        else {
            LinearGradient(
                boundingBox.left,
                boundingBox.top,
                boundingBox.left,
                boundingBox.bottom,
                boxGradientStartColor,
                boxGradientEndColor,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(
            boundingBox,
            boxCornerRadius.toFloat(),
            boxCornerRadius.toFloat(),
            boxPaint
        )
    }
}