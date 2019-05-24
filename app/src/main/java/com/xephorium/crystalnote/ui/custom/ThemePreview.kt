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
    private var headerHeight: Int? = null
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

        if (textHeight == null) textHeight = (scaleUnit!! * .3).toInt()
        if (headerHeight == null) headerHeight = (scaleUnit!! * .4).toInt()
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
                    (paddingLarge!! + (scaleUnit!! * 1.7)).toFloat(),
                    (currentVerticalPosition + headerHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
            currentVerticalPosition += (headerHeight!! + paddingLarge!!)
        }

        // Card 1
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 5, 4)

        // Card 2
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 4, 2)

        // Header 2
        if (SHOW_HEADERS) {
            paint.color = ContextCompat.getColor(context, R.color.lightTextSecondary)
            canvas?.drawRoundRect(
                    (paddingLarge!!).toFloat(),
                    (currentVerticalPosition + paddingMedium!!).toFloat(),
                    (paddingLarge!! + (scaleUnit!! * 1.3)).toFloat(),
                    (currentVerticalPosition + paddingMedium!! + headerHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
            currentVerticalPosition += (headerHeight!! + paddingMedium!! + paddingLarge!!)
        }

        // Card 3
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 6, 3)

        // Card 4
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 3, 1)

        // Card 5
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 5, 4)

        // Card 6
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 4, 2)

        // Card 7
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 6, 3)

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
            currentVerticalPosition: Int,
            titleLength: Int,
            previewLines: Int
    ): Int {

        val textTitleHeight = textHeight!! + (paddingMedium!! * 2)
        val textLineHeight = textHeight!! + paddingSmall!!
        val lines = if (previewLines > MAX_LINES) MAX_LINES else previewLines
        val viewHeight = textTitleHeight + (lines * textLineHeight) + paddingSmall!!

        // Background
        paint.color = ContextCompat.getColor(context, R.color.lightNoteBackground)
        canvas?.drawRoundRect(
                (paddingLarge!!).toFloat(),
                currentVerticalPosition.toFloat(),
                (viewWidth!! - (paddingLarge!!)).toFloat(),
                (currentVerticalPosition + viewHeight).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Title
        paint.color = ContextCompat.getColor(context, R.color.lightTextPrimary)
        canvas?.drawRoundRect(
                (paddingLarge!! + paddingMedium!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (paddingLarge!! + paddingMedium!! + (scaleUnit!! * titleLength)).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Date
        if (DATE_TYPE != DateType.NONE) {
            paint.color = ContextCompat.getColor(context, R.color.lightTextTertiary)
            canvas?.drawRoundRect(
                    (viewWidth!! - (paddingLarge!! + paddingMedium!!) - (scaleUnit!! * 1.7)).toFloat(),
                    (currentVerticalPosition + paddingMedium!!).toFloat(),
                    (viewWidth!! - (paddingLarge!! + paddingMedium!!)).toFloat(),
                    (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
        }

        // Lines
        paint.color = ContextCompat.getColor(context, R.color.lightTextSecondary)
        for (x in 0 until lines) {
            val lineEndPosition = viewWidth!! - (paddingLarge!! + paddingMedium!!)
            val baseHeight = currentVerticalPosition + textTitleHeight + (x * textLineHeight)
            canvas?.drawRoundRect(
                    (paddingLarge!! + paddingMedium!!).toFloat(),
                    (baseHeight).toFloat(),
                    (lineEndPosition).toFloat(),
                    (baseHeight + textHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
        }

        return viewHeight + paddingMedium!!
    }


    /*--- Constants ---*/

    companion object {
        private const val STROKE_WIDTH = 3.toFloat()
        private const val CORNER_RADIUS = 3.toFloat()

        private const val SHOW_HEADERS = false
        private const val SHOW_COLOR_BAR = true
        private const val MAX_LINES = 2
        private val DATE_TYPE = DateType.DYNAMIC
    }
}