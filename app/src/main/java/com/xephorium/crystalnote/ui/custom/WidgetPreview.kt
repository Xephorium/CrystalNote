package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.DateType


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
    private var paddingMedium: Int? = null
    private var paddingSmall: Int? = null
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

        if (viewHeight == null) {
            val height = measuredHeight
            viewHeight = if (height % 2 == 0) height else height - 1
        }
        if (viewWidth == null) {
            val width = (viewHeight!! * (4.0/5.5)).toInt()
            viewWidth = if (width % 2 == 0) width else width - 1
        }
        if (scaleUnit == null) scaleUnit = (viewWidth!! / 12)

        if (titleHeight == null) titleHeight = (scaleUnit!! * 0.85 * textScale).toInt()
        if (textHeight == null) textHeight = (scaleUnit!! * 0.75 * textScale).toInt()
        if (textBulletRadius == null) textBulletRadius = (scaleUnit!! * 0.25 * textScale).toInt()

        if (paddingBorder == null) paddingBorder = (scaleUnit!! * 1.1).toInt()
        if (paddingMedium == null) paddingMedium = (scaleUnit!! * 0.5).toInt()
        if (paddingSmall == null) paddingSmall = (scaleUnit!! * 0.35).toInt()
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
                (viewWidth!!).toFloat(),
                (viewHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Title
        paint.color = theme.colorTextPrimary
        canvas?.drawRoundRect(
                (paddingBorder!! - paddingTiny!!).toFloat(),
                (paddingBorder!! + paddingSmall!!).toFloat(),
                (paddingBorder!! - paddingTiny!! + titleHeight!! * 5).toFloat(),
                (paddingBorder!! + paddingSmall!! + titleHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += (paddingBorder!! + paddingSmall!! + titleHeight!! + paddingSmall!!)

        // List Bullet #1
        paint.color = theme.colorTextSecondary
        canvas?.drawCircle(
                (paddingBorder!! + (textBulletRadius!! / 2)).toFloat(),
                (currentVerticalPosition + paddingBorder!! + (textHeight!! / 2)).toFloat(),
                (textBulletRadius!!).toFloat(),
                paint
        )

        // List Item #1
        paint.color = theme.colorTextSecondary
        var xValue = viewWidth!! * 0.7
        var xBound: Int = if (xValue < viewWidth!! - paddingBorder!!) xValue.toInt()
        else viewWidth!! - paddingBorder!!
        canvas?.drawRoundRect(
                (paddingBorder!! + paddingSmall!! + (textBulletRadius!! * 2)).toFloat(),
                (currentVerticalPosition + paddingBorder!!).toFloat(),
                (xBound).toFloat(),
                (currentVerticalPosition + paddingBorder!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += textHeight!! + paddingBorder!!

        // List Bullet #2
        paint.color = theme.colorTextSecondary
        canvas?.drawCircle(
                (paddingBorder!! + (textBulletRadius!! / 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!! + (textHeight!! / 2)).toFloat(),
                (textBulletRadius!!).toFloat(),
                paint
        )

        // List Item #2
        paint.color = theme.colorTextSecondary
        xValue = viewWidth!! * 0.65
        xBound = if (xValue < viewWidth!! - paddingBorder!!) xValue.toInt()
        else viewWidth!! - paddingBorder!!
        canvas?.drawRoundRect(
                (paddingBorder!! + paddingSmall!! + (textBulletRadius!! * 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (xBound).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += (textHeight!! + paddingMedium!!) * 2

        // List Bullet #3
        paint.color = theme.colorTextSecondary
        canvas?.drawCircle(
                (paddingBorder!! + (textBulletRadius!! / 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!! + (textHeight!! / 2)).toFloat(),
                (textBulletRadius!!).toFloat(),
                paint
        )

        // List Item #3
        paint.color = theme.colorTextSecondary
        xValue = viewWidth!! * 0.67
        xBound = if (xValue < viewWidth!! - paddingBorder!!) xValue.toInt()
        else viewWidth!! - paddingBorder!!
        canvas?.drawRoundRect(
                (paddingBorder!! + paddingSmall!! + (textBulletRadius!! * 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (xBound).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += textHeight!! + paddingMedium!!

        // List Bullet #4
        paint.color = theme.colorTextSecondary
        canvas?.drawCircle(
                (paddingBorder!! + (textBulletRadius!! / 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!! + (textHeight!! / 2)).toFloat(),
                (textBulletRadius!!).toFloat(),
                paint
        )

        // List Item #4
        paint.color = theme.colorTextSecondary
        xValue = viewWidth!! * 0.55
        xBound = if (xValue < viewWidth!! - paddingBorder!!) xValue.toInt()
        else viewWidth!! - paddingBorder!!
        canvas?.drawRoundRect(
                (paddingBorder!! + paddingSmall!! + (textBulletRadius!! * 2)).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (xBound).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += (textHeight!! + paddingMedium!!) * 2

        // Paragraph Line 1
        paint.color = theme.colorTextSecondary
        canvas?.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (viewWidth!! - paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += textHeight!! + paddingMedium!!

        // Paragraph Line 2
        paint.color = theme.colorTextSecondary
        canvas?.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (viewWidth!! - paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += textHeight!! + paddingMedium!!

        // Paragraph Line 3
        paint.color = theme.colorTextSecondary
        canvas?.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (viewWidth!! - paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        currentVerticalPosition += textHeight!! + paddingMedium!!

        // Paragraph Line 4
        paint.color = theme.colorTextSecondary
        xValue = viewWidth!! * 0.73
        xBound = if (xValue < viewWidth!! - paddingBorder!!) xValue.toInt()
        else viewWidth!! - paddingBorder!!
        canvas?.drawRoundRect(
                (paddingBorder!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (xBound).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
        invalidate()
    }


    /*--- Private Methods ---*/


    /*--- Constants ---*/

    companion object {
        private const val CORNER_RADIUS = 5.toFloat()
    }
}