package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils
import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.CornerCurve
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency


class WidgetPreview : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var scaleUnit: Int? = null

    private var titleHeight: Int? = null
    private var textHeight: Int? = null
    private var textBulletRadius: Int? = null

    private var paddingBorder: Int? = null
    private var paddingLineGap: Int? = null
    private var paddingBulletLineStart: Int? = null
    private var paddingTitleGap: Int? = null
    private var paddingTiny: Int? = null

    private var textScale = DEFAULT_TEXT_SCALE
    private var transparency = WidgetState.DEFAULT_TRANSPARENCY
    private var backgroundColor = WidgetState.DEFAULT_BACKGROUND_COLOR
    private var titleColor = WidgetState.DEFAULT_TITLE_COLOR
    private var textColor = WidgetState.DEFAULT_CONTENT_COLOR
    private var cornerCurve = WidgetState.DEFAULT_CORNER_CURVE


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {
        paint.style = FILL
        paint.isAntiAlias = true
    }


    /*--- Lifecycle Methods ---*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Calculate View Dimensions & Scale
        if (viewHeight == null)
            viewHeight = if (measuredHeight % 2 == 0) measuredHeight else measuredHeight - 1
        if (viewWidth == null) {
            val width = (viewHeight!! * (8.0/11.7)).toInt()
            viewWidth = if (width % 2 == 0) width else width - 1
        }
        if (scaleUnit == null) scaleUnit = (viewWidth!! / 12)

        // Calculate Text Size
        if (titleHeight == null) titleHeight = (scaleUnit!! * 0.9).toInt()
        if (textHeight == null) textHeight = (scaleUnit!! * 0.80).toInt()
        if (textBulletRadius == null) textBulletRadius = (scaleUnit!! * 0.3).toInt()

        // Calculate Element Size
        if (paddingBorder == null) paddingBorder = (scaleUnit!! * 1.1).toInt()
        if (paddingLineGap == null) paddingLineGap = (scaleUnit!! * 0.45).toInt()
        if (paddingBulletLineStart == null) paddingBulletLineStart = (scaleUnit!! * 0.35).toInt()
        if (paddingTitleGap == null) paddingTitleGap = (scaleUnit!! * 1.2).toInt()
        if (paddingTiny == null) paddingTiny = (scaleUnit!! * 0.1).toInt()


        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas) {

        var currentVerticalPosition = 0

        // Background
        paint.color = getColorWithTransparencyApplied(backgroundColor, transparency.value)
        canvas.drawRoundRect(
                0.toFloat(),
                0.toFloat(),
                viewWidth!!.toFloat(),
                viewHeight!!.toFloat(),
                (cornerCurve.value * 2).toFloat(),
                (cornerCurve.value * 2).toFloat(),
                paint
        )

        // Title
        paint.color = titleColor
        canvas.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (paddingBorder!! + (paddingTiny!! * 3)).toFloat(),
                (viewWidth!! * 0.4).toFloat(),
                (paddingBorder!! + (paddingTiny!! * 3) + (titleHeight!! * textScale)).toFloat(),
                ((titleHeight!! * textScale) / 2).toFloat(),
                ((titleHeight!! * textScale) / 2).toFloat(),
                paint
        )
        currentVerticalPosition += paddingBorder!! + (paddingTiny!! * 3) +
                (titleHeight!! * textScale).toInt() + paddingTitleGap!!

        // List Bullet #1
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            textColor
        )

        // List Bullet #2
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            textColor
        )

        // Blank Line
        currentVerticalPosition += textHeight!! + paddingLineGap!!

        // List Bullet #3
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            textColor
        )

        // List Bullet #4
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            textColor
        )

        // Blank Line
        currentVerticalPosition += textHeight!! + paddingLineGap!!

        // Paragraph Line #1
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            textColor
        )

        // Paragraph Line #2
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            textColor
        )

        // Paragraph Line #3
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            textColor
        )

        // Paragraph Line #4
        drawLine(
            canvas,
            currentVerticalPosition,
            false,
            0.8,
            textColor
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTextSize(textSize: TextSize) {
        textScale = DEFAULT_TEXT_SCALE
        when (textSize) {
            TextSize.Tiny -> textScale -= (TEXT_SCALE_VARIATION * 2)
            TextSize.Small -> textScale -= TEXT_SCALE_VARIATION
            TextSize.Medium -> Unit
            TextSize.Large -> textScale += TEXT_SCALE_VARIATION
            TextSize.Huge -> textScale += (TEXT_SCALE_VARIATION * 2)
        }
        invalidate()
    }

    fun setTransparency(transparency: Transparency) {
        this.transparency = transparency
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        invalidate()
    }

    fun setTitleColor(color: Int) {
        titleColor = color
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    fun setCornerCurve(curve: CornerCurve) {
        cornerCurve = curve
        invalidate()
    }


    /*--- Private Methods ---*/

    private fun drawLine(
        canvas: Canvas?,
        currentVerticalPosition: Int,
        displayBullet: Boolean,
        lineWidth: Double,
        color: Int
    ): Int {

        // Calculate Widget Borders
        val leftBorder = paddingBorder!!
        val rightBorder = viewWidth!! - paddingBorder!!

        // Calculate Bullet Positions
        val bulletStart = leftBorder + ((textBulletRadius!! * textScale) / 2) + (paddingTiny!! * 2)
        val bulletTop = currentVerticalPosition + ((textHeight!! * textScale) / 2)

        // Calculate Line Positions
        var lineEnd: Int = (viewWidth!! * lineWidth).toInt()
        lineEnd = if (lineEnd < rightBorder) lineEnd else rightBorder
        val lineStart: Int = if (!displayBullet) leftBorder
        else leftBorder + paddingBulletLineStart!! + ((textBulletRadius!! * textScale).toInt() * 2)
        val lineTop = currentVerticalPosition
        val lineBottom = currentVerticalPosition + (textHeight!! * textScale).toInt()

        // Set Paint Color
        paint.color = color

        // Draw Bullet
        if (displayBullet) {
            canvas?.drawCircle(
                bulletStart.toFloat(),
                bulletTop.toFloat(),
                (textBulletRadius!! * textScale).toFloat(),
                paint
            )
        }

        // Draw Line
        canvas?.drawRoundRect(
            lineStart.toFloat(),
            lineTop.toFloat(),
            lineEnd.toFloat(),
            lineBottom.toFloat(),
            ((textHeight!! * textScale) / 2).toFloat(),
            ((textHeight!! * textScale) / 2).toFloat(),
            paint
        )

        return lineBottom + paddingLineGap!!
    }

    private fun getColorWithTransparencyApplied(color: Int, transparency: Double): Int {
        val c = Color.parseColor("#" + Integer.toHexString(color).substring(2))
        return ColorUtils.setAlphaComponent(c, ((1.0 - transparency) * 255.0).toInt())
    }


    /*--- Constants ---*/

    companion object {
        private const val CORNER_RADIUS = 5.toFloat()
        private const val DEFAULT_TEXT_SCALE = 1.05
        private const val TEXT_SCALE_VARIATION = 0.1
    }
}