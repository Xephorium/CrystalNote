package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogContract.Presenter.Companion.DEFAULT_CUSTOM_COLOR
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor


class RainbowView : View {


    /*--- Variables ---*/

    private var paint = Paint()
    private var verticalShader: Shader? = null
    private var horizontalShader: Shader? = null
    private var combinedShader: Shader? = null

    private var colorWhite = Color.WHITE
    private var colorBlack = Color.BLACK
    private var colorBase = Color.HSVToColor(floatArrayOf(
        DEFAULT_CUSTOM_COLOR.hue.toFloat(),
        1f,
        1f
    ))
    private var colorPrecise = DEFAULT_CUSTOM_COLOR

    private var theme = CrystalNoteTheme.default(context)

    private var dotPositionX: Float? = null
    private var dotPositionY: Float? = null
    private var dotRadius = 8f
    private var dotColor = theme.colorAccent


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = ColorOrb.OUTLINE_WIDTH
    }


    /*--- Lifecycle Methods ---*/

    override fun onDraw(canvas: Canvas) {
        initializeShaders()

        // Draw Background
        paint.setShader(combinedShader)
        canvas.drawRoundRect(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            8f,
            8f,
            paint
        )

        // Draw Position Dot
        dotPositionX?.let { posX ->
            dotPositionY?.let { posY ->
                paint.color = dotColor
                paint.shader = null
                canvas.drawCircle(
                    posX,
                    posY,
                    dotRadius,
                    paint
                )
            }
        }

        super.onDraw(canvas)
    }


    /*--- Public Methods ---*/

    fun updateColor(color: PreciseColor) {
        colorPrecise = color
        colorBase = getColorFromHSV(color.hue.toFloat(), 1f, 1f)

        refreshShaders()
        refreshDotPosition()
        invalidate()
    }

    fun notifyVisible() {
        refreshShaders()
        refreshDotPosition()
        invalidate()
    }


    /*--- Private Methods ---*/

    private fun refreshShaders() {
        verticalShader = LinearGradient(
            0f,
            0f,
            0f,
            measuredHeight.toFloat(),
            colorWhite,
            colorBlack,
            TileMode.CLAMP
        )
        horizontalShader = LinearGradient(
            0f,
            0f,
            measuredWidth.toFloat(),
            0f,
            colorWhite,
            colorBase,
            TileMode.CLAMP
        )

        combinedShader = ComposeShader(
            verticalShader!!,
            horizontalShader!!,
            PorterDuff.Mode.MULTIPLY
        )
    }

    private fun refreshDotPosition() {
        dotPositionX = (measuredWidth - 2 * dotRadius) * (colorPrecise.saturation / 100f) + dotRadius
        dotPositionY = (measuredHeight - 2 * dotRadius) * (1f - (colorPrecise.value / 100f)) + dotRadius
    }

    private fun initializeShaders() {
        if (verticalShader == null)
            verticalShader = LinearGradient(
                0f,
                0f,
                0f,
                measuredHeight.toFloat(),
                colorWhite,
                colorBlack,
                TileMode.CLAMP
            )

        if (horizontalShader == null)
            horizontalShader = LinearGradient(
                0f,
                0f,
                measuredWidth.toFloat(),
                0f,
                colorWhite,
                colorBase,
                TileMode.CLAMP
            )

        if (combinedShader == null)
            combinedShader = ComposeShader(
                verticalShader!!,
                horizontalShader!!,
                PorterDuff.Mode.MULTIPLY
            )
    }

    private fun getColorFromHSV(hue: Float, sat: Float, value: Float): Int {
        return Color.HSVToColor(floatArrayOf(hue, sat, value))
    }

}