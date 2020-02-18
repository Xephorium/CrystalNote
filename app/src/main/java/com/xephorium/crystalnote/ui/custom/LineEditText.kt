package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.extensions.getThemeColor

class LineEditText : EditText {


    /*--- Declare Variables ---*/

    private lateinit var paint: Paint
    private var showUnderline = true
    private var useMonospacedFont = false


    /*--- Line Edit Text Setup ---*/

    constructor(context: Context) : super(context) {
        initializeLineEditText(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeLineEditText(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initializeLineEditText(context)
    }

    private fun initializeLineEditText(context: Context) {
        this.paint = Paint()
        this.background = TRANSPARENT
        this.gravity = Gravity.TOP
        this.inputType = INPUT_TYPE
        this.isScrollContainer = false
        this.isVerticalScrollBarEnabled = false

        paint.style = Paint.Style.STROKE
        paint.color = context.getThemeColor(R.attr.themeNoteUnderline)
    }


    /*--- Lifecycle Methods ---*/

    override fun onDraw(canvas: Canvas) {

        // Draw Underline
        if (showUnderline) {
            val viewHeight = if (height > computeVerticalScrollRange()) height
            else computeVerticalScrollRange()
            val numLines = (viewHeight - paddingTop - paddingBottom) / lineHeight

            val offset = if (useMonospacedFont) UNDERLINE_VERTICAL_OFFSET_MONO
            else UNDERLINE_VERTICAL_OFFSET

            for (x in 0 until numLines + 1) {
                val lineYPos = lineHeight * (x + 1) + paddingTop - offset
                canvas.drawLine(
                        (left + paddingLeft).toFloat(),
                        lineYPos.toFloat(),
                        (right - paddingRight).toFloat(),
                        lineYPos.toFloat(), paint
                )
            }
        }

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun showUnderline() {
        showUnderline = true
        invalidate()
    }

    fun hideUnderline() {
        showUnderline = false
        invalidate()
    }

    fun useMonospacedFont() {
        useMonospacedFont = true
        this.typeface = ResourcesCompat.getFont(context, R.font.roboto_mono)
        this.textSize = pixelsToSp(resources.getDimension(R.dimen.textUpdateMono))
    }


    /*--- Private Methods ---*/

    fun pixelsToSp(px: Float): Float {
        return px / resources.displayMetrics.scaledDensity
    }


    /*--- Constants ---*/

    companion object {
        val TRANSPARENT = ColorDrawable(0x00FFFFFF)
        const val UNDERLINE_VERTICAL_OFFSET = 4
        const val UNDERLINE_VERTICAL_OFFSET_MONO = 8
        const val INPUT_TYPE = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    }
}