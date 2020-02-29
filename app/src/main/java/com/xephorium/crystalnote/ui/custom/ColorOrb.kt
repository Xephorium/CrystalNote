package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


class ColorOrb : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var theme = CrystalNoteTheme.default(context)
    private var orbColor = DEFAULT_ORB_COLOR
    private var showSelector = false


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = OUTLINE_WIDTH
    }


    /*--- Lifecycle Methods ---*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (viewHeight == null) {
            val height = measuredHeight
            viewHeight = if (height % 2 == 0) height else height - 1
        }
        if (viewWidth == null) viewWidth = viewHeight!!

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        // Conditional Selection Highlight
        if (showSelector) {
            paint.color = theme.colorTextSecondary
            paint.style = FILL
            canvas?.drawCircle(
                (viewWidth!! / 2.0).toFloat(),
                (viewHeight!! / 2.0).toFloat(),
                ((viewWidth!! * RADIUS_SELECTION) / 2.0).toFloat(),
                paint
            )
        }

        // Color Circle
        paint.color = orbColor
        paint.style = FILL
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * RADIUS_COLOR) / 2.0).toFloat(),
            paint
        )

        // Outline
        paint.color = theme.colorTextSecondary
        paint.style = STROKE
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * RADIUS_COLOR) / 2.0).toFloat(),
            paint
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
        invalidate()
    }

    fun setColor(color: Int) {
        orbColor = color
        invalidate()
    }

    fun showSelector() {
        showSelector = true
        invalidate()
    }

    fun hideSelector() {
        showSelector = false
        invalidate()
    }


    /*--- Constants ---*/

    companion object {
        val DEFAULT_ORB_COLOR = Color.parseColor("#000000")
        const val RADIUS_SELECTION = 1.0
        const val RADIUS_COLOR = 0.525
        const val OUTLINE_WIDTH = (3.0).toFloat()
    }
}