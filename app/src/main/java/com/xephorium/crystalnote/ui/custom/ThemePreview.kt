package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.xephorium.crystalnote.R


class ThemePreview : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var padding: Int? = null
    private var textHeight: Int? = null


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {

        // Setup Paints
        paint.style = STROKE
        paint.strokeWidth = STROKE_WIDTH
        paint.isAntiAlias = true
    }


    /*--- Lifecycle Methods ---*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (viewHeight == null) {
            val height = measuredHeight
            viewHeight = if (height % 2 == 0) height else height - 1
        }
        if (viewWidth == null) {
            val width = ((viewHeight!! * 9.0) / 15).toInt()
            viewWidth = if (width % 2 == 0) width else width - 1
        }
        if (padding == null) padding = (viewWidth!! / 12)
        if (textHeight == null) textHeight = (padding!! * .5).toInt()

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        // Background
        this.background = ContextCompat.getDrawable(context, R.color.lightBackground)

        // Toolbar
        paint.color = ContextCompat.getColor(context, R.color.red500)
        paint.style = FILL
        canvas?.drawRect(
                0.toFloat(),
                0.toFloat(),
                (viewWidth!!).toFloat(),
                (padding!! * 2.5).toFloat(),
                paint
        )

        // Toolbar Icon
        paint.color = ContextCompat.getColor(context, R.color.whiteSmokeAlpha)
        canvas?.drawRoundRect(
                (padding!! * .75).toFloat(),
                (padding!! * .75).toFloat(),
                (padding!! * 1.75).toFloat(),
                (padding!! * 1.75).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Toolbar Title
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas?.drawRoundRect(
                (padding!! * 2.5).toFloat(),
                (padding!! * .75).toFloat(),
                (padding!! * 7.5).toFloat(),
                (padding!! * 1.75).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        if (SHOW_HEADERS) {

            // Header 1
            paint.color = ContextCompat.getColor(context, R.color.lightTextSecondary)
            canvas?.drawRoundRect(
                    (padding!! * .75).toFloat(),
                    (padding!! * 3.5).toFloat(),
                    (padding!! * 3.25).toFloat(),
                    (padding!! * 3.5 + textHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
        }

        // Floating Action Button
        paint.color = ContextCompat.getColor(context, R.color.red500)
        canvas?.drawCircle(
                (viewWidth!! - (padding!! * 2.2)).toFloat(),
                (viewHeight!! - (padding!! * 2.2)).toFloat(),
                (padding!! * 1.2).toFloat(),
                paint
        )


        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/


    /*--- Constants ---*/

    companion object {
        private const val STROKE_WIDTH = 3.toFloat()
        private const val CORNER_RADIUS = 3.toFloat()

        private const val SHOW_HEADERS = true
    }
}