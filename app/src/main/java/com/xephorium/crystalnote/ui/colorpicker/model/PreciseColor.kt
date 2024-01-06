package com.xephorium.crystalnote.ui.colorpicker.model

import android.graphics.Color
import android.graphics.Color.HSVToColor
import android.graphics.Color.colorToHSV
import kotlin.math.roundToInt


/**
 * ## Precise Color
 *
 *   This class exists to store custom hsv color state for the ColorPickerDialog
 *   and reduce data loss issues while converting between various color formats.
 *
 *   - Hue - Integer `[0, 360]`
 *   - Saturation - Integer `[0, 100]`
 *   - Value - Integer `[0, 100]`
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

    constructor(color: Int) : this () {
        val hsv = getHsvFromIntColor(color)
        this.hue = hsv.first
        this.saturation = hsv.second
        this.value = hsv.third
    }


    /*--- Class Overrides ---*/

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreciseColor

        if (hue != other.hue) return false
        if (saturation != other.saturation) return false
        return value == other.value
    }

    fun copy(): PreciseColor {
        return PreciseColor(hue, saturation, value)
    }


    /*--- Object Conversion Methods ---*/

    fun toIntColor(): Int {
        return HSVToColor(floatArrayOf(
                hue.toFloat(),
                (saturation.toDouble() / 100.0).toFloat(),
                (value.toDouble() / 100.0).toFloat()
            )
        )
    }

    fun toHexString(): String {
        return String.format("%06X", 0xFFFFFF and toIntColor()).lowercase()
    }


    /*--- Class Properties ---*/

    companion object {
        val DEFAULT_PRECISE_COLOR = PreciseColor(212, 75, 100)


        /*--- Class Conversion Methods ---*/

        fun fromHex(hex: String) : PreciseColor? {
            val intColor = getIntColorFromHex(hex)
            return intColor?.let {
                val hsv = getHsvFromIntColor(it)
                PreciseColor(
                    hsv.first,
                    hsv.second,
                    hsv.third
                )
            }
        }

        /*--- Private Utility Methods ---*/

        private fun getHsvFromIntColor(color: Int): Triple<Int, Int, Int> {
            val floatArray = FloatArray(3)
            colorToHSV(color, floatArray)
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
}