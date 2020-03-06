package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
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
    private var colorBarWidth: Int? = null
    private var actionButtonRadius: Int? = null

    private var paddingLarge: Int? = null
    private var paddingMedium: Int? = null
    private var paddingSmall: Int? = null

    private var theme = CrystalNoteTheme.default(context)
    private var maxLines = 5
    private var dateType = DateType.DYNAMIC
    private var showColorBar = true
    private var themeColorBar = true
    private var showHeaders = true


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
            val width = ((viewHeight!! * 9.0) / 15).toInt()
            viewWidth = if (width % 2 == 0) width else width - 1
        }
        if (scaleUnit == null) scaleUnit = (viewWidth!! / 12)

        if (toolbarHeight == null) toolbarHeight = (scaleUnit!! * 2.25).toInt()
        if (headerHeight == null) headerHeight = (scaleUnit!! * .3).toInt()
        if (textHeight == null) textHeight = (scaleUnit!! * .25).toInt()
        if (colorBarWidth == null) colorBarWidth = (scaleUnit!! * .35).toInt()
        if (actionButtonRadius == null) actionButtonRadius = (scaleUnit!! * 1).toInt()

        if (paddingLarge == null) paddingLarge = (scaleUnit!! * .75).toInt()
        if (paddingMedium == null) paddingMedium = (scaleUnit!! * .5).toInt()
        if (paddingSmall == null) paddingSmall = (scaleUnit!! * .25).toInt()

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        var currentVerticalPosition = 0

        // Background
        this.setBackgroundColor(theme.colorBackground)

        // Toolbar
        paint.color = theme.colorToolbar
        canvas?.drawRect(
                0.toFloat(),
                0.toFloat(),
                (viewWidth!!).toFloat(),
                (toolbarHeight!!).toFloat(),
                paint
        )
        currentVerticalPosition += (toolbarHeight!! + scaleUnit!!)

        // Toolbar Icon
        paint.color = theme.colorToolbarTextSecondary
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
        paint.color = theme.colorToolbarTextPrimary
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
        if (showHeaders) {
            paint.color = theme.colorTextSecondary
            canvas?.drawRoundRect(
                    (paddingLarge!!).toFloat(),
                    currentVerticalPosition.toFloat(),
                    (paddingLarge!! + (scaleUnit!! * 1.5)).toFloat(),
                    (currentVerticalPosition + headerHeight!!).toFloat(),
                    CORNER_RADIUS,
                    CORNER_RADIUS,
                    paint
            )
            currentVerticalPosition += (headerHeight!! + paddingLarge!!)
        }

        // Card 1
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 4, 5, 0)

        // Card 2
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 3, 2, 1)

        // Header 2
        if (showHeaders) {
            paint.color = theme.colorTextSecondary
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
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 5, 3, 2)

        // Card 4
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 3, 1, 3)

        // Card 5
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 4, 4, 4)

        // Card 6
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 3, 2, 5)

        // Card 7
        currentVerticalPosition += drawNoteCard(canvas, paint, currentVerticalPosition, 5, 3, 6)

        // Floating Action Button
        paint.color = theme.colorAccent
        canvas?.drawCircle(
                (viewWidth!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (viewHeight!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (actionButtonRadius!!).toFloat(),
                paint
        )


        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
        invalidate()
    }

    fun setPreviewLines(lines: Int) {
        maxLines = lines
        invalidate()
    }

    fun setDateType(type: DateType) {
        dateType = type
        invalidate()
    }

    fun setNoteColorBarVisible(visible: Boolean) {
        showColorBar = visible
        invalidate()
    }

    fun setNoteColorBarThemed(themed: Boolean) {
        themeColorBar = themed
        invalidate()
    }

    fun setHeadersVisible(visible: Boolean) {
        showHeaders = visible
        invalidate()
    }


    /*--- Private Methods ---*/

    private fun drawNoteCard(
            canvas: Canvas?,
            paint: Paint,
            currentVerticalPosition: Int,
            titleLength: Int,
            previewLines: Int,
            index: Int
    ): Int {

        val textTitleHeight = textHeight!! + (paddingMedium!! * 2)
        val textLineHeight = textHeight!! + paddingSmall!!
        val lines = if (previewLines > maxLines) maxLines else previewLines
        val viewHeight = textTitleHeight + (lines * textLineHeight) + paddingSmall!!
        val colorBarDisplayWidth = if (showColorBar) colorBarWidth!! else 0

        // Background
        paint.color = theme.colorNoteBackground
        canvas?.drawRoundRect(
                (paddingLarge!!).toFloat(),
                currentVerticalPosition.toFloat(),
                (viewWidth!! - (paddingLarge!!)).toFloat(),
                (currentVerticalPosition + viewHeight).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Color Bar
        paint.color = if (themeColorBar) theme.colorNoteColorBar else NOTE_COLORS[index]
        canvas?.drawRoundRect(
                (paddingLarge!!).toFloat(),
                currentVerticalPosition.toFloat(),
                (paddingLarge!! + colorBarDisplayWidth).toFloat(),
                (currentVerticalPosition + viewHeight).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )
        canvas?.drawRect(
                (paddingLarge!! + (colorBarDisplayWidth / 2)).toFloat(),
                currentVerticalPosition.toFloat(),
                (paddingLarge!! + colorBarDisplayWidth).toFloat(),
                (currentVerticalPosition + viewHeight).toFloat(),
                paint
        )

        // Title
        paint.color = theme.colorTextPrimary
        canvas?.drawRoundRect(
                (colorBarDisplayWidth + paddingLarge!! + paddingMedium!!).toFloat(),
                (currentVerticalPosition + paddingMedium!!).toFloat(),
                (paddingLarge!! + paddingMedium!! + (scaleUnit!! * titleLength)).toFloat(),
                (currentVerticalPosition + paddingMedium!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
        )

        // Date
        if (dateType != DateType.NONE) {
            paint.color = theme.colorTextTertiary
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
        paint.color = theme.colorTextSecondary
        for (x in 0 until lines) {
            val lineEndPosition = viewWidth!! - (paddingLarge!! + paddingMedium!!)
            val baseHeight = currentVerticalPosition + textTitleHeight + (x * textLineHeight)
            canvas?.drawRoundRect(
                    (colorBarDisplayWidth + paddingLarge!! + paddingMedium!!).toFloat(),
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
        private const val CORNER_RADIUS = 3.toFloat()

        private val NOTE_COLORS = listOf(
                Color.parseColor("#7aa4d1"), //    Azure Light
                Color.parseColor("#98cdaa"), //    Green Light
                Color.parseColor("#eeee8c"), //    Yellow Light
                Color.parseColor("#fbb065"), //    Orange Light
                Color.parseColor("#ec9393"), //    Red Light
                Color.parseColor("#d6a9c0"), //    Thanos Light
                Color.parseColor("#caa8f0")  //    Barney Light
        )
    }
}