package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.util.TypedValue
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
    private var orbColor = theme.colorTextSecondary
    private var orbContrast = 2.0
    private var padding: Float = DEFAULT_ORB_PADDING
    private var useContrastOutline = true
    private var forceThickOutline = false

    private var viewPadding: Float = 0f
    private var thickOutlineStrokeWidth: Float
    private var thickOutlineGapWidth: Float

    private var outlinePaintColor: Int? = null


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = OUTLINE_WIDTH

        // Calculate DP Dimens
        calculateViewPadding()
        thickOutlineStrokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            THICK_OUTLINE_RADIUS,
            resources.displayMetrics
        )
        thickOutlineGapWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            THICK_OUTLINE_GAP,
            resources.displayMetrics
        )

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

    override fun onDraw(canvas: Canvas) {

        // Calculate Padding
        val overdrawPadding = 0.90 // (1.0 - padding)

        // Calculate Outline Paint Color
        if (outlinePaintColor == null) {
            outlinePaintColor = determineOutlineColor()
        }

        if (forceThickOutline) {

            // Thick Outline
            outlinePaintColor?.let {
                paint.color = it
                paint.style = STROKE
                paint.strokeWidth = thickOutlineStrokeWidth
                canvas.drawCircle(
                    (viewWidth!! / 2.0).toFloat(),
                    (viewHeight!! / 2.0).toFloat(),
                    ((viewWidth!! * OUTLINE_RADIUS * overdrawPadding - viewPadding) / 2.0).toFloat(),
                    paint
                )
            }

            // Background Circle
            backgroundColor?.let {
                paint.color = it
                paint.style = FILL
                canvas.drawCircle(
                    (viewWidth!! / 2.0).toFloat(),
                    (viewHeight!! / 2.0).toFloat(),
                    ((viewWidth!! * OUTLINE_RADIUS * overdrawPadding - viewPadding) / 2.0).toFloat(),
                    paint
                )
            }

            // Foreground Circle
            paint.color = orbColor
            paint.style = FILL
            canvas.drawCircle(
                (viewWidth!! / 2.0).toFloat(),
                (viewHeight!! / 2.0).toFloat(),
                ((viewWidth!! * OUTLINE_RADIUS * overdrawPadding - thickOutlineGapWidth - viewPadding) / 2.0).toFloat(),
                paint
            )

        } else {

            // Outline
            outlinePaintColor?.let {
                paint.color = it
                paint.style = STROKE
                paint.strokeWidth = OUTLINE_WIDTH
                canvas.drawCircle(
                    (viewWidth!! / 2.0).toFloat(),
                    (viewHeight!! / 2.0).toFloat(),
                    ((viewWidth!! * OUTLINE_RADIUS * overdrawPadding - viewPadding) / 2.0).toFloat(),
                    paint
                )
            }

            // Color Circle
            paint.color = orbColor
            paint.style = FILL
            canvas.drawCircle(
                (viewWidth!! / 2.0).toFloat(),
                (viewHeight!! / 2.0).toFloat(),
                ((viewWidth!! * OUTLINE_RADIUS * overdrawPadding - viewPadding) / 2.0).toFloat(),
                paint
            )
        }

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

    fun setOutlineAlpha(alpha: Double?) {
        outlineAlpha = alpha?.let { (alpha * 255).toInt() }
        outlinePaintColor = determineOutlineColor()
    }

    fun resetOutlineState() {
        outlineColor = null
        outlineAlpha = null
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

    fun setPadding(dimenResource: Int) {
        padding = getResourceDp(dimenResource)
        calculateViewPadding()
        invalidate()
    }

    fun enableForcedThickOutline() {
        forceThickOutline = true
        setOutlineAlpha(1.0)
        setBackdropColor(CrystalNoteTheme.fromCurrentTheme(context).colorToolbar)
        outlinePaintColor = determineOutlineColor()
        invalidate()
    }

    fun disableForcedThickOutline() {
        forceThickOutline = false
        setOutlineAlpha(null)
        setBackdropColor(CrystalNoteTheme.fromCurrentTheme(context).colorToolbar)
        outlinePaintColor = determineOutlineColor()
        invalidate()
    }


    /*--- Private Methods ---*/

    fun getResourceDp(dimenResource: Int): Float {
        return resources.getDimension(dimenResource) / resources.displayMetrics.density

    }

    fun calculateViewPadding() {
        viewPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            padding,
            resources.displayMetrics
        )
    }

    private fun determineOrbContrast(): Double {
        if (backgroundColor != null) {
            return ColorUtility.calculateContrastRatio(backgroundColor!!, orbColor)
        } else {
            return ColorUtility.calculateContrastRatio(theme.colorNoteBackground, orbColor)
        }
    }

    private fun determineOutlineColor(): Int {
        return if ((orbContrast < CONTRAST_THRESHOLD && useContrastOutline) || forceThickOutline) {
            val drawColor = outlineColor ?: theme.colorTextPrimary
            val drawAlpha = outlineAlpha ?: OUTLINE_ALPHA
            ColorUtils.setAlphaComponent(drawColor, drawAlpha)
        } else {
            orbColor
        }
    }


    /*--- Constants ---*/

    companion object {
        val DEFAULT_ORB_PADDING = 15f
        const val CONTRAST_THRESHOLD = 1.3
        const val OUTLINE_RADIUS = 0.9
        const val OUTLINE_WIDTH = (5.0).toFloat()
        const val OUTLINE_ALPHA = (0.2 * 255).toInt()
        const val THICK_OUTLINE_RADIUS = 3.5f
        const val THICK_OUTLINE_GAP = 3.0f
    }
}