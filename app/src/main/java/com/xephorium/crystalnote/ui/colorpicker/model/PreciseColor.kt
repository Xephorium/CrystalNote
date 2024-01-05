package com.xephorium.crystalnote.ui.colorpicker.model

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt


/**
 * **Precise Color**
 *
 * This class exists to reduce data loss issues while converting
 * between Java's various color classes and their disparate storage
 * strategies on disk. PreciseColor is driven by the following three
 * variables:
 *
 * - Hue - Integer (0, 360)
 * - Saturation - Integer (0, 100)
 * - Value - Integer (0, 100)
 *
 * All state updates are made to these three variables and all format
 * conversions are derived from them. This produces more stable results
 * than relying on the limited resolution of HEX codes or floating point
 * imprecision of standard HSV/HSL Color classes.
 *
 */
class PreciseColor() {


    /*--- Variable Declarations ---*/

    var hue: Int = 212
        set(hue) { field = hue.coerceIn(0, 360) }

    var saturation: Int = 100
        set(sat) { field = sat.coerceIn(0, 100) }

    var value: Int = 75
        set(value) { field = value.coerceIn(0, 360) }


    /*--- Constructors ---*/

    constructor(hue: Int, saturation: Int, value: Int) : this() {
        this.hue = hue
        this.saturation = saturation
        this.value = value
    }


    /*--- Conversion Methods ---*/

    fun setFromHex(hex: String): Boolean {
        return getIntColorFromHex(hex)?.let {
            val hsv = getHsvFromIntColor(it)
            hue = hsv.first
            saturation = hsv.second
            value = hsv.third
            true
        } ?: false
    }

    fun getIntColor(): Int {
        return ColorUtils.HSLToColor(floatArrayOf(
            hue.toFloat(),
            (saturation.toDouble() / 100.0).toFloat(),
            (value.toDouble() / 100.0).toFloat()
        ))
    }

    fun getHexCode(): String {
        return String.format("%06X", 0xFFFFFF and getIntColor()).lowercase()
    }


    /*--- Private Methods ---*/

    private fun getHsvFromIntColor(color: Int): Triple<Int, Int, Int> {
        val floatArray = FloatArray(3)
        ColorUtils.colorToHSL(color, floatArray)
        return Triple(
            floatArray[0].toInt(),
            (floatArray[1] * 100).roundToInt(),
            (floatArray[2] * 100).roundToInt(),
        )
    }

    private fun getIntColorFromHex(hex: String): Int? {
        var color: Int? = null
        try {
            var sanitizedHex = hex.replace("#", "")
            sanitizedHex = convertThreeDigitHexToSix(sanitizedHex)
            color = Color.parseColor("#$sanitizedHex")
        } catch (exception: Exception) { /* Do Nothing */ }
        return color
    }

    private fun convertThreeDigitHexToSix(hex: String): String {
        return if(hex.length == 3) {
            StringBuilder()
                .append(hex[0])
                .append(hex[0])
                .append(hex[1])
                .append(hex[1])
                .append(hex[2])
                .append(hex[2])
                .toString()
        } else {
            hex
        }
    }
}