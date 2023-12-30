package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.View
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


class NotePreviewView : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var scaleUnit: Int? = null

    private var toolbarHeight: Int? = null
    private var toolbarTitleWidth: Int? = null

    private var textHeight: Int? = null
    private var lineHeight: Int? = null
    private var underlineOffset: Int? = null
    private var underlineThickness: Int? = null
    private var actionButtonRadius: Int? = null

    private var paddingLarge: Int? = null
    private var paddingMedium: Int? = null

    private var theme = CrystalNoteTheme.default(context)
    private var showUnderline = false
    private var showScrollButton = true
    private var showColorOrb = true
    private var showMonospacedFont = false


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
        if (toolbarTitleWidth == null) toolbarTitleWidth = (scaleUnit!! * 2.5).toInt()
        if (textHeight == null) textHeight = (scaleUnit!! * .3).toInt()
        if (lineHeight == null) lineHeight = (scaleUnit!! * .65).toInt()
        if (underlineOffset == null) underlineOffset = (scaleUnit!! * .4).toInt()
        if (underlineThickness == null) underlineThickness = (scaleUnit!! * .05).toInt()
        if (actionButtonRadius == null) actionButtonRadius = (scaleUnit!! * 0.6).toInt()

        if (paddingLarge == null) paddingLarge = (scaleUnit!! * .8).toInt()
        if (paddingMedium == null) paddingMedium = (scaleUnit!! * .55).toInt()

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas) {

        var currentVerticalPosition = 0

        // Background
        paint.color = theme.colorNoteBackground
        canvas.drawRoundRect(
            0f,
            0f,
            (viewWidth!!).toFloat(),
            (viewHeight!! + 1).toFloat(),
            CORNER_RADIUS,
            CORNER_RADIUS,
            paint
        )

        // Toolbar
        paint.color = theme.colorToolbar
        canvas.drawRoundRect(
            0f,
            0f,
            (viewWidth!!).toFloat(),
            (toolbarHeight!!).toFloat(),
            CORNER_RADIUS,
            CORNER_RADIUS,
            paint
        )
        canvas.drawRect(
            0f,
            (toolbarHeight!! / 2).toFloat(),
            (viewWidth!!).toFloat(),
            (toolbarHeight!!).toFloat(),
            paint
        )
        currentVerticalPosition += (toolbarHeight!! + scaleUnit!!)

        // Toolbar Icon Left
        paint.color = theme.colorToolbarTextSecondary
        canvas.drawRoundRect(
            (paddingLarge!!).toFloat(),
            (paddingLarge!!).toFloat(),
            (toolbarHeight!! - paddingLarge!!).toFloat(),
            (toolbarHeight!! - paddingLarge!!).toFloat(),
            CORNER_RADIUS,
            CORNER_RADIUS,
            paint
        )

        // Toolbar Icon Right
        paint.color = theme.colorToolbarTextSecondary
        canvas.drawRoundRect(
            (viewWidth!! - toolbarHeight!! + paddingLarge!!).toFloat(),
            (paddingLarge!!).toFloat(),
            (viewWidth!! - paddingLarge!!).toFloat(),
            (toolbarHeight!! - paddingLarge!!).toFloat(),
            CORNER_RADIUS,
            CORNER_RADIUS,
            paint
        )

        // Toolbar Color Orb
        if (showColorOrb) {
            paint.color = HomePreviewView.NOTE_COLORS[3]
            canvas.drawRoundRect(
                (viewWidth!! - 2 * toolbarHeight!! + 2 * paddingLarge!!).toFloat(),
                (paddingLarge!!).toFloat(),
                (viewWidth!! - toolbarHeight!!).toFloat(),
                (toolbarHeight!! - paddingLarge!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
            )
        }

        // Toolbar Title
        paint.color = theme.colorToolbarTextPrimary
        canvas.drawRoundRect(
            ((viewWidth!! / 2) - (toolbarTitleWidth!! / 2)).toFloat(),
            (paddingLarge!!).toFloat(),
            ((viewWidth!! / 2) + (toolbarTitleWidth!! / 2)).toFloat(),
            (toolbarHeight!! - paddingLarge!!).toFloat(),
            CORNER_RADIUS,
            CORNER_RADIUS,
            paint
        )

        // Note Content
        val baseHeight = toolbarHeight!! + paddingMedium!!
        for (x in LINE_WIDTHS.indices) {

            // Text
            paint.color = theme.colorTextSecondary
            val lineWidth = if (showMonospacedFont) getMonospacedLineLength(LINE_WIDTHS[x]) else LINE_WIDTHS[x]
            canvas.drawRoundRect(
                (paddingLarge!!).toFloat(),
                (baseHeight + x * lineHeight!!).toFloat(),
                (paddingLarge!! + (viewWidth!! - 2 * paddingLarge!!) * lineWidth).toFloat(),
                (baseHeight + x * lineHeight!! + textHeight!!).toFloat(),
                CORNER_RADIUS,
                CORNER_RADIUS,
                paint
            )

            // Underline
            if (showUnderline) {
                paint.color = theme.colorNoteUnderline
                canvas.drawRect(
                    (paddingLarge!!).toFloat(),
                    (baseHeight + x * lineHeight!! + underlineOffset!!).toFloat(),
                    (viewWidth!! - paddingLarge!!).toFloat(),
                    (baseHeight + x * lineHeight!! + underlineOffset!! + underlineThickness!!).toFloat(),
                    paint
                )
            }
        }

        // Floating Action Button
        if (showScrollButton) {
            paint.color = theme.colorBackground
            canvas.drawCircle(
                (viewWidth!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (viewHeight!! - (actionButtonRadius!! + paddingLarge!!)).toFloat(),
                (actionButtonRadius!!).toFloat(),
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

    fun setUnderlineVisible(visible: Boolean) {
        showUnderline = visible
        invalidate()
    }

    fun setScrollButtonVisible(visible: Boolean) {
        showScrollButton = visible
        invalidate()
    }

    fun setColorOrbVisible(visible: Boolean) {
        showColorOrb = visible
        invalidate()
    }

    fun setMonospaceFontVisible(visible: Boolean) {
        showMonospacedFont = visible
        invalidate()
    }


    /*--- Private Methods ---*/

    private fun getMonospacedLineLength(value: Double): Double {
        return if (value == 0.0) 0.0
        else if (value >= .8) 1.0
        else value + 0.1
    }


    /*--- Constants ---*/

    companion object {
        private const val CORNER_RADIUS = 8.toFloat()

        private val LINE_WIDTHS = listOf(
            0.3,
            0.35,
            0.4,
            0.25,
            0.0,
            1.0,
            0.9,
            0.95,
            1.0,
            0.95,
            0.85,
            0.5,
            0.0,
            0.8,
            0.95,
            0.85,
            1.0,
            0.95,
            0.85,
            0.8,
            0.2,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
        )
    }
}