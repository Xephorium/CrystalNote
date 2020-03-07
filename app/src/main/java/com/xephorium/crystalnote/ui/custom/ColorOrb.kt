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
    private var backgroundColor: Int? = null
    private var outlineColor: Int? = null
    private var outlineAlpha: Int? = null
    private var orbColor = DEFAULT_ORB_COLOR
    private var orbContrast = 2.0
    private var padding: Double = 0.0
    private var useContrastOutline = true

    private var outlinePaintColor: Int? = null


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
        if (outlinePaintColor == null) {
            outlinePaintColor = determineOutlineColor()
        }
        paint.color = outlinePaintColor!!

        // Outline
        paint.style = STROKE
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * OUTLINE_RADIUS * pad) / 2.0).toFloat(),
            paint
        )

        // Color Circle
        paint.color = orbColor
        paint.style = FILL
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * OUTLINE_RADIUS * pad) / 2.0).toFloat(),
            paint
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
        invalidate()
    }

    fun setBackdropColor(color: Int) {
        backgroundColor = color
        orbContrast = determineOrbContrast()
        outlinePaintColor = determineOutlineColor()
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
        outlinePaintColor = determineOutlineColor()
    }

    fun setOutlineAlpha(alpha: Double) {
        outlineAlpha = (alpha * 255).toInt()
        outlinePaintColor = determineOutlineColor()
    }

    fun setColor(color: Int) {
        orbColor = color
        orbContrast = determineOrbContrast()
        outlinePaintColor = determineOutlineColor()
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


    /*--- Private Methods ---*/

    private fun determineOrbContrast(): Double {
        if (backgroundColor != null) {
            return ColorUtility.calculateContrastRatio(backgroundColor!!, orbColor)
        } else {
            return ColorUtility.calculateContrastRatio(theme.colorNoteBackground, orbColor)
        }
    }

    private fun determineOutlineColor(): Int {
        if (orbContrast < CONTRAST_THRESHOLD && useContrastOutline) {
            val drawColor = outlineColor ?: theme.colorTextPrimary
            val drawAlpha = outlineAlpha ?: OUTLINE_ALPHA
            return ColorUtils.setAlphaComponent(drawColor, drawAlpha)
        } else {
            return orbColor
        }
    }


    /*--- Constants ---*/

    companion object {
        val DEFAULT_ORB_COLOR = Color.parseColor("#000000")
        const val CONTRAST_THRESHOLD = 1.1
        const val OUTLINE_RADIUS = 0.9
        const val OUTLINE_WIDTH = (5.0).toFloat()
        const val OUTLINE_ALPHA = (0.2 * 255).toInt()
    }
}