package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


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

    private var theme = CrystalNoteTheme.default(context)
    private var textScale = 1.0


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
        if (titleHeight == null) titleHeight = (scaleUnit!! * 0.9 * textScale).toInt()
        if (textHeight == null) textHeight = (scaleUnit!! * 0.80 * textScale).toInt()
        if (textBulletRadius == null) textBulletRadius = (scaleUnit!! * 0.3 * textScale).toInt()

        // Calculate Element Size
        if (paddingBorder == null) paddingBorder = (scaleUnit!! * 1.1).toInt()
        if (paddingLineGap == null) paddingLineGap = (scaleUnit!! * 0.45).toInt()
        if (paddingBulletLineStart == null) paddingBulletLineStart = (scaleUnit!! * 0.35).toInt()
        if (paddingTitleGap == null) paddingTitleGap = (scaleUnit!! * 1.2).toInt()
        if (paddingTiny == null) paddingTiny = (scaleUnit!! * 0.1).toInt()


        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        var currentVerticalPosition = 0

        // Background
        paint.color = theme.colorBackground
        canvas?.drawRoundRect(
                0.toFloat(),
                0.toFloat(),
                viewWidth!!.toFloat(),
                viewHeight!!.toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Title
        paint.color = theme.colorTextPrimary
        canvas?.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (paddingBorder!! + (paddingTiny!! * 3)).toFloat(),
                (viewWidth!! * 0.4).toFloat(),
                (paddingBorder!! + (paddingTiny!! * 3) + titleHeight!!).toFloat(),
                (titleHeight!! / 2).toFloat(),
                (titleHeight!! / 2).toFloat(),
                paint
        )
        currentVerticalPosition += paddingBorder!! + (paddingTiny!! * 3) +
                titleHeight!! + paddingTitleGap!!

        // List Bullet #1
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            theme.colorTextSecondary
        )

        // List Bullet #2
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            theme.colorTextSecondary
        )

        // Blank Line
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            1.0,
            theme.colorBackground
        )

        // List Bullet #3
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            theme.colorTextSecondary
        )

        // List Bullet #4
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            0.6,
            theme.colorTextSecondary
        )

        // Blank Line
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            true,
            1.0,
            theme.colorBackground
        )

        // Paragraph Line #1
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            theme.colorTextSecondary
        )

        // Paragraph Line #2
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            theme.colorTextSecondary
        )

        // Paragraph Line #3
        currentVerticalPosition = drawLine(
            canvas,
            currentVerticalPosition,
            false,
            1.0,
            theme.colorTextSecondary
        )

        // Paragraph Line #4
        drawLine(
            canvas,
            currentVerticalPosition,
            false,
            0.8,
            theme.colorTextSecondary
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
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
        val bulletStart = leftBorder + (textBulletRadius!! / 2) + (paddingTiny!! * 2)
        val bulletTop = currentVerticalPosition + (textHeight!! / 2)

        // Calculate Line Positions
        var lineEnd: Int = (viewWidth!! * lineWidth).toInt()
        lineEnd = if (lineEnd < rightBorder) lineEnd else rightBorder
        val lineStart: Int = if (!displayBullet) leftBorder
        else leftBorder + paddingBulletLineStart!! + (textBulletRadius!! * 2)
        val lineTop = currentVerticalPosition
        val lineBottom = currentVerticalPosition + textHeight!!

        // Set Paint Color
        paint.color = color

        // Draw Bullet
        if (displayBullet) {
            canvas?.drawCircle(
                bulletStart.toFloat(),
                bulletTop.toFloat(),
                (textBulletRadius!!).toFloat(),
                paint
            )
        }

        // Draw Line
        canvas?.drawRoundRect(
            lineStart.toFloat(),
            lineTop.toFloat(),
            lineEnd.toFloat(),
            lineBottom.toFloat(),
            (textHeight!! / 2).toFloat(),
            (textHeight!! / 2).toFloat(),
            paint
        )

        return lineBottom + paddingLineGap!!
    }


    /*--- Constants ---*/

    companion object {
        private const val CORNER_RADIUS = 5.toFloat()
    }
}