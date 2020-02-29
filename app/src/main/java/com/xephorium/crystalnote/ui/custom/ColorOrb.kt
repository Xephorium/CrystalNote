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


class ColorOrb : View {


    /*--- Variable Declarations ---*/

    private val paint = Paint()
    private var viewHeight: Int? = null
    private var viewWidth: Int? = null
    private var theme = CrystalNoteTheme.default(context)
    private var orbColor = DEFAULT_ORB_COLOR
    private var showSelector = false


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
        if (viewWidth == null) viewWidth = viewHeight!!

        setMeasuredDimension(viewWidth!!, viewHeight!!)
    }

    override fun onDraw(canvas: Canvas?) {

        // Draw Selection Highlight
        if (showSelector) {
            paint.color = theme.colorTextTertiary
            canvas?.drawCircle(
                (viewWidth!! / 2.0).toFloat(),
                (viewHeight!! / 2.0).toFloat(),
                ((viewWidth!! * SIZE_SELECTION) / 2.0).toFloat(),
                paint
            )
        }

        // Draw Outline
        paint.color = theme.colorTextTertiary
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * SIZE_OUTLINE) / 2.0).toFloat(),
            paint
        )

        // Draw Color
        paint.color = orbColor
        canvas?.drawCircle(
            (viewWidth!! / 2.0).toFloat(),
            (viewHeight!! / 2.0).toFloat(),
            ((viewWidth!! * SIZE_COLOR) / 2.0).toFloat(),
            paint
        )

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun setTheme(newTheme: CrystalNoteTheme) {
        theme = newTheme
        invalidate()
    }

    fun setColor(color: Int) {
        orbColor = color
        invalidate()
    }

    fun showSelector() {
        showSelector = true
        invalidate()
    }

    fun hideSelector() {
        showSelector = false
        invalidate()
    }


    /*--- Constants ---*/

    companion object {
        val DEFAULT_ORB_COLOR = Color.parseColor("#000000")
        const val SIZE_SELECTION = 1.0
        const val SIZE_OUTLINE = 0.6
        const val SIZE_COLOR = 0.525
    }
}