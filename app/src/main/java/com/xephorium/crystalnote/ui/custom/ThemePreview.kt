package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.DateType


class ThemePreview : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var scaleUnit: Int? = null

    private var toolbarHeight: Int? = null
    private var textHeight: Int? = null
    private var actionButtonRadius: Int? = null

    private var paddingLarge: Int? = null
    private var paddingMedium: Int? = null
    private var paddingSmall: Int? = null


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
        if (scaleUnit == null) scaleUnit = (viewWidth!! / 12)

        if (textHeight == null) textHeight = (scaleUnit!! * .5).toInt()
        if (toolbarHeight == null) toolbarHeight = (scaleUnit!! * 2.25).toInt()
        if (actionButtonRadius == null) actionButtonRadius = (scaleUnit!! * 1).toInt()

        if (paddingLarge == null) paddingLarge = (scaleUnit!! * .75).toInt()
        if (paddingMedium == null) paddingMedium = (scaleUnit!! * .5).toInt()
        if (paddingSmall == null) paddingSmall = (scaleUnit!! * .25).toInt()

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        var currentVerticalPosition = 0

        // Background
        this.background = ContextCompat.getDrawable(context, R.color.lightBackground)

        // Toolbar
        paint.color = ContextCompat.getColor(context, R.color.red500)
        paint.style = FILL
        canvas?.drawRect(
                0.toFloat(),
                0.toFloat(),
                (viewWidth!!).toFloat(),
                (toolbarHeight!!).toFloat(),
                paint
        )
        currentVerticalPosition += (toolbarHeight!! + scaleUnit!!)

        // Toolbar Icon
        paint.color = ContextCompat.getColor(context, R.color.whiteSmokeAlpha)
        canvas?.drawRoundRect(
                (paddingLarge!!).toFloat(),
                (paddingLarge!!).toFloat(),
                (toolbarHeight!! - paddingLarge!!).toFloat(),
                (toolbarHeight!! - paddingLarge!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Toolbar Title
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas?.drawRoundRect(
                (toolbarHeight!!).toFloat(),
                (paddingLarge!!).toFloat(),
                (toolbarHeight!! + (scaleUnit!! * 5.5)).toFloat(),
                (toolbarHeight!! - paddingLarge!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Header 1
        if (SHOW_HEADERS) {
            paint.color = ContextCompat.getColor(context, R.color.lightTextSecondary)
            canvas?.drawRoundRect(
                    (paddingLarge!!).toFloat(),
                    currentVerticalPosition.toFloat(),
                    (paddingLarge!! + (scaleUnit!! * 2.5)).toFloat(),
                    (currentVerticalPosition + textHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
            currentVerticalPosition += (textHeight!! + paddingLarge!!)
        }

        // Card 1
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition)

        // Floating Action Button
        paint.color = ContextCompat.getColor(context, R.color.red500)
        canvas?.drawCircle(
                (viewWidth!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (viewHeight!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (actionButtonRadius!!).toFloat(),
                paint
        )


        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/


    /*--- Private Methods ---*/

    private fun drawNoteCard(
            canvas: Canvas?,
            paint: Paint,
            currentVerticalPosition: Int
    ): Int {

        val textLineHeight = textHeight!! + (scaleUnit!! * (.5))
        val viewHeight = (scaleUnit!! * (1)) + (NOTE_LINES * textLineHeight)

        // Background
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas?.drawRoundRect(
                (scaleUnit!! * .75).toFloat(),
                currentVerticalPosition.toFloat(),
                (viewWidth!! - (scaleUnit!! * .75)).toFloat(),
                (currentVerticalPosition + viewHeight).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Title
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas?.drawRoundRect(
                (scaleUnit!! * 1.25).toFloat(),
                (currentVerticalPosition + (scaleUnit!! * .5)).toFloat(),
                (viewWidth!! - (scaleUnit!! * .75)).toFloat(),
                (currentVerticalPosition + (scaleUnit!! * .5) + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        return 5
    }


    /*--- Constants ---*/

    companion object {
        private const val STROKE_WIDTH = 3.toFloat()
        private const val CORNER_RADIUS = 3.toFloat()

        private const val SHOW_HEADERS = true
        private const val SHOW_COLOR_BAR = true
        private val DATE_TYPE = DateType.DYNAMIC
        private const val NOTE_LINES = 3
    }
}