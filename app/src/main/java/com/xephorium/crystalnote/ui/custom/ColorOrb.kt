package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.utility.ColorUtility


class ColorOrb : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()

    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var desiredViewHeight: Int? = null

    private var theme = CrystalNoteTheme.default(context)
    private var orbColor = DEFAULT_ORB_COLOR
    private var orbContrast = 2.0
    private var padding: Double = 0.0
    private var useContrastOutline = true


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

        desiredViewHeight?.let {
            val height = it
            viewHeight = if (height % 2 == 0) height else height - 1
            viewWidth = viewHeight!!
        }

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        // Calculate Padding
        val pad = (1.0 - padding)

        // Set Outline Paint Color
        if (orbContrast < CONTRAST_THRESHOLD && useContrastOutline) {
            paint.color = ColorUtils.setAlphaComponent(theme.colorTextPrimary, OUTLINE_ALPHA)
        } else {
            paint.color = orbColor
        }

        // Outline
        paint.style = STROKE
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * RADIUS_COLOR * pad) / 2.0).toFloat(),
            paint
        )

        // Color Circle
        paint.color = orbColor
        paint.style = FILL
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * RADIUS_COLOR * pad) / 2.0).toFloat(),
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
        orbContrast = if (theme.colorNoteBackground == color) 1.0 else 2.0
        invalidate()
    }

    fun getColor(): Int {
        return orbColor
    }

    fun setSizeByResourceId(sizeResource: Int) {
        desiredViewHeight = resources.getDimensionPixelSize(sizeResource)
        invalidate()
    }

    fun setSizeByPixelValue(pixelValue: Int) {
        desiredViewHeight = pixelValue
        invalidate()
    }

    fun setPadding(newPadding: Double) {
        padding = newPadding
    }

    fun showContrastOutline(show: Boolean) {
        useContrastOutline = show
    }


    /*--- Constants ---*/

    companion object {
        val DEFAULT_ORB_COLOR = Color.parseColor("#000000")
        const val CONTRAST_THRESHOLD = 1.1
        const val RADIUS_COLOR = 0.9
        const val OUTLINE_WIDTH = (5.0).toFloat()
        const val OUTLINE_ALPHA = (0.2 * 255).toInt()
    }
}