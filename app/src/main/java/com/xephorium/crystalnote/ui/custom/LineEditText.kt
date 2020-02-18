package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import androidx.core.content.ContextCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.extensions.getThemeColor

class LineEditText : EditText {


    /*--- Declare Variables ---*/

    private lateinit var paint: Paint
    private var showUnderline = true


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
        if (showUnderline) {
            val viewHeight = if (height > computeVerticalScrollRange()) height
            else computeVerticalScrollRange()
            val numLines = (viewHeight - paddingTop - paddingBottom) / lineHeight

            for (x in 0 until numLines + 1) {
                val lineYPos = lineHeight * (x + 1) + paddingTop
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


    /*--- Constants ---*/

    companion object {
        val TRANSPARENT = ColorDrawable(0x00FFFFFF)
        const val INPUT_TYPE = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    }
}