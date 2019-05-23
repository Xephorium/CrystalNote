package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.xephorium.crystalnote.R


class ThemePreview : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var logicalDensity: Float? = null
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var padding: Int? = null


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {

        // Setup Paints
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH
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

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        this.background = ContextCompat.getDrawable(context, R.color.white)
        paint.color = ContextCompat.getColor(context, R.color.red500)

        canvas?.drawRect(
                padding!!.toFloat(),
                padding!!.toFloat(),
                (viewWidth!! - padding!!).toFloat(),
                (viewHeight!! - padding!!).toFloat(),
                paint
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/




    /*--- Constants ---*/

    companion object {
        private const val STROKE_WIDTH = 3.toFloat()
    }
}